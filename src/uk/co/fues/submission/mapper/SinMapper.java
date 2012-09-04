package uk.co.fues.submission.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;
import org.commoncrawl.hadoop.mapred.ArcRecord;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import uk.co.fues.submission.MoneyIsTheRootOfAllEvil;
import uk.co.fues.submission.classifier.MultiNomialBayes;
import uk.co.fues.submission.trainer.parse.DataCleansing;
import uk.co.fues.submission.util.datastructure.DoubleArrayWritable;
import uk.co.fues.submission.vocabulary.Sins;
import uk.co.fues.submission.vocabulary.Money;

public class SinMapper extends MapReduceBase implements
		Mapper<Text, ArcRecord, DoubleWritable, DoubleArrayWritable> {

	private static final Logger LOG = Logger
			.getLogger(MoneyIsTheRootOfAllEvil.class);
	private MultiNomialBayes classifer;
	public SinMapper() {
		try {
			classifer = new MultiNomialBayes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
	}

    public void map(Text key, ArcRecord value, OutputCollector<DoubleWritable, DoubleArrayWritable> output, Reporter reporter)
            throws IOException {

          try {

            if (!value.getContentType().contains("html")) {
              return;
            }

            // just curious how many of each content type we've seen

            // ensure sample instances have enough memory to parse HTML
            if (value.getContentLength() > (5 * 1024 * 1024)) {
              return;
            }

            // Count all 'itemtype' attributes referencing 'schema.org'
            Document doc = value.getParsedHTML();

            if (doc == null) {
              return;
            }

            Elements mf = doc.select("[itemtype~=schema.org]");			// Get the text content as a string.
			String pageText = doc.text();
			String cleansed = DataCleansing.clean(pageText);
			if(!DataCleansing.include(cleansed)) {
				return;
			}

//			output.collect(new DoubleWritable((int)(getIDF(pageText, Money.MONEY.getVocab())*100)),
//					getSinVector(pageText));
			output.collect(getMoneyness(cleansed),
					getClassiferSinVector(cleansed));
		} catch (Exception ex) {
			LOG.error("Caught Exception", ex);
		}
	}
	
	private double getIDF(String text, List<String> terms) {
		double count = 0;
		String [] words = text.split(" ");
		for (String word : words) {
			if(terms.contains(word)) {
				count++;
				//System.out.println(word + " : " + text);
			}
		}
		count=count==0?0:(count/words.length);
		//if(count>0) {System.out.println(count);}
		return count;
	}
	
	
	private DoubleArrayWritable getClassiferSinVector(String text) throws Exception {
		double[] results = classifer.classifyMessage(text);
		DoubleWritable[] init = new DoubleWritable[7];
		for(int i = 0; i<7; i++) {
			init[i] = new DoubleWritable(results[i]);
		}
		return new DoubleArrayWritable(init);
	}
	
	private DoubleWritable getMoneyness(String text) throws Exception {
		double[] results = classifer.classifyMessage(text);
		return new DoubleWritable((int) (results[7]*10));
	}
	
	@SuppressWarnings("unused")
	private DoubleArrayWritable getSinVector(String text) {
		DoubleWritable[] init = new DoubleWritable[7];
		for(int i=0; i<SINS.size(); i++) {
			init[i] = new DoubleWritable(getIDF(text, SINS.get(i)));
		}
		return new DoubleArrayWritable(init);
	}

	
	DecimalFormat keyFormat = new DecimalFormat("#.#");
	
	@SuppressWarnings("unchecked")
	public static final List<List<String>> SINS = Arrays.asList(Sins.LUST.getVocab(), Sins.GREED.getVocab(), Sins.GLUTTONY.getVocab(), Sins.ENVY.getVocab(), Sins.PRIDE.getVocab(), Sins.WRATH.getVocab(), Sins.SLOTH.getVocab());

}
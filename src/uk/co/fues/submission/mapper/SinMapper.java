package uk.co.fues.submission.mapper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.log4j.Logger;

import uk.co.fues.submission.MoneyIsTheRootOfAllEvil;
import uk.co.fues.submission.util.datastructure.DoubleArrayWritable;
import uk.co.fues.submission.vocabulary.Sins;
import uk.co.fues.submission.vocabulary.Money;

public class SinMapper extends MapReduceBase implements
		Mapper<Text, Text, DoubleWritable, DoubleArrayWritable> {

	private static final Logger LOG = Logger
			.getLogger(MoneyIsTheRootOfAllEvil.class);

	public void map(Text key, Text value,
			OutputCollector<DoubleWritable, DoubleArrayWritable> output, Reporter reporter)
			throws IOException {
		try {
			// Get the text content as a string.
			String pageText = value.toString();

			// Removes all punctuation.
			pageText = pageText.replaceAll("[^a-zA-Z0-9 ]", "");

			// Normalizes whitespace to single spaces.
			pageText = pageText.replaceAll("\\s+", " ");

			output.collect(new DoubleWritable((int)(getIDF(pageText, Money.MONEY.getVocab())*100)),
					getSinVector(pageText));
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
				System.out.println(word + " : " + text);
			}
		}
		count=count==0?0:(count/words.length);
		//if(count>0) {System.out.println(count);}
		return count;
	}
	
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
package uk.co.fues.submission;

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

			output.collect(new DoubleWritable(Double.valueOf(keyFormat.format(getIDF(pageText, MONEY)*100))),
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

	
	DecimalFormat keyFormat = new DecimalFormat("#.##");
	public static final List<String> MONEY = Arrays.asList("money", "finance", "cost", "cash", "bank", "wage", "salary", "earning", "dollars", "pay", "price"); // index 1
	public static final List<String> LUST = Arrays.asList("appetite", "passion", "lust", "desire", "craving", "yearning", "thirst"); // index 1
	public static final List<String> GREED = Arrays.asList("greed", "want", "voracious", "greedy", "excess"); // index 1
	public static final List<String> GLUTTONY = Arrays.asList("indulge","excess", "gluttony", "more", "endless", "limitless", "infinite"); // index 1
	public static final List<String> ENVY = Arrays.asList("jealous, jealousy", "envy", "green eyed", "envious"); // index 1
	public static final List<String> PRIDE = Arrays.asList("rightful", "want", "mine", "proud"); // index 1
	public static final List<String> WRATH = Arrays.asList("rage, anger", "angry", "resentment", "mad", "revenge", "vengance", "indignant"); // index 1
	public static final List<String> SLOTH = Arrays.asList("sloth, lazy", "easy", "shortcut", "quick", "fast", "easily"); // index 1
	public static final List<List<String>> SINS = Arrays.asList(LUST, GREED, GLUTTONY, ENVY, PRIDE, WRATH, SLOTH);

}
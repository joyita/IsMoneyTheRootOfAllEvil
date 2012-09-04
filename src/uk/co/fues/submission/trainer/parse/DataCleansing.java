package uk.co.fues.submission.trainer.parse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataCleansing {

	public static String clean(String text) {
		return trimShortSentanceSequences(removeRepeatedSentances(text));
	}
	
	public static boolean include(String text) {
		if(text.split(" ").length>500) {
			return true;
		}
		return false;
	}
	
	private static Set<String> removeRepeatedSentances(String text) {
		String [] paras = text.split("\n+");
		List<String> paragraphs = Arrays.asList(paras);
		for(int i = 0; i<paragraphs.size(); i++) {
			String paragraph = paragraphs.get(i);
			paragraphs.set(i, clearWhitespace(paragraph));
		}
		Set<String> ret = new HashSet<String>();
		for(String para:paragraphs) {
			ret.add(para);
		}
		ret.remove("");
		return ret;
	}
	
	private static String trimShortSentanceSequences(Set<String> paragraphs) {
		List<String> sens = new ArrayList<String>();
		List<Integer> counts = new ArrayList<Integer>();
		int cachecount = 0;
		for(String sentances:paragraphs) {
			counts.add(sentances.split(" ").length);
			sens.add(sentances); 
		}
		List<String> killindex = new ArrayList<String>();
		for(int i = 0; i<counts.size(); i++) {
			Integer in = counts.get(i);
			// arbitary decision, 15 words = short parapgraph.
			if(in<30) {
				cachecount++;
			}
			// 3 short sentances in a row.
			if(cachecount>2) {
				killindex.add(sens.get(i));
				killindex.add(sens.get(i-1));
				killindex.add(sens.get(i-2));
				cachecount = 0;
			}
		}
		for(String index:killindex) {
			sens.remove(index);
		}
		// rebuild document
		StringBuilder builder = new StringBuilder();
		for(String sen:sens) {
			// "\n" was used as the paragraph splitter
			builder.append(sen + "\n"); 
		}
		return builder.toString();
	}
	
	private static String clearWhitespace(String data) {
		data = data.replaceAll("\\s+", " ");
		return data;
	}
}

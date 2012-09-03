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
		return removeSingleSentanceSequences(removeRepeatedSentances(text));
	}
	
	public static boolean include(String text) {
		if(text.split(" ").length>300) {
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
	
	private static String removeSingleSentanceSequences(Set<String> text) {
		// get single sentances in a row, likely to be menu items
		List<String> sens = new ArrayList<String>();
		List<Integer> counts = new ArrayList<Integer>();
		int cachecount = 0;
		for(String sentances:text) {
			counts.add(sentances.split(" ").length);
			sens.add(sentances); //just making efficient use of iteration to build list
		}
		List<String> killindex = new ArrayList<String>();
		for(int i = 0; i<counts.size(); i++) {
			Integer in = counts.get(i);
			if(in<15) {
				cachecount++;
			}
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
		
		StringBuilder builder = new StringBuilder();
		for(String sen:sens) {
			builder.append(sen + "\n");
		}
		return builder.toString();
		
	}
	
	private static String clearWhitespace(String data) {
		data = data.replaceAll("\\s+", " ");
		return data;
	}
}

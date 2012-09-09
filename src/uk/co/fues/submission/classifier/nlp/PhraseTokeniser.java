package uk.co.fues.submission.classifier.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.tokenizers.Tokenizer;

public class PhraseTokeniser extends Tokenizer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8434873053681311753L;

	Logger log = LoggerFactory.getLogger(getClass());

	PhraseExtractor extractor;
	List<String> phrases;
	Queue<String> list;
	
	public PhraseTokeniser() {
		extractor = new PhraseExtractor();
		phrases = new ArrayList<String>();
		list = new PriorityQueue<String>();
	}
	
	@Override
	public String getRevision() {
		return "0.0.1";
	}

	@Override
	public String globalInfo() {
		return "Phrase tokeniser that returns noun & verb phrases from a document using POS parsing";
	}

	@Override
	public boolean hasMoreElements() {
		return !list.isEmpty();
	}

	@Override
	public Object nextElement() {
		return list.poll();
	}

	@Override
	public void tokenize(String s) {
		phrases = extractor.extractPhrases(s);
		list.addAll(phrases);
	}

}

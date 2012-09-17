package uk.co.fues.submission.util.parsing.cleansing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.tokenizers.WordTokenizer;

public class JoinedUpSplitter {

	Logger log = LoggerFactory.getLogger(getClass());
	SpellChecker spellChecker;
	WordTokenizer tokensier;

	public JoinedUpSplitter() {
		try {
			tokensier = new WordTokenizer();
			tokensier.setDelimiters(" \\r\\n\\t.,;:\"()?!");
			spellChecker = new SpellChecker(new RAMDirectory(), new LevensteinDistance());
			@SuppressWarnings("deprecation")
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_20, null);
			spellChecker.indexDictionary(new PlainTextDictionary(new File("/workspace/Money Is The Root Of All Evil/resources/dictionary/fulldictionary00.txt")),
							config, true);
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	public Map<String, Integer> getSplit(String sentance) {
		Map<String, Integer> split = new HashMap<String, Integer>();
		int out = -1;
		try {
				
				getSplit(sentance, split, 0, 0);
				
			return split;
		} catch (IOException e) {
			log.error("", e);
			return null;
		}
	}
	
	public void getSplit(String sentance, Map<String, Integer> sequence, int step, int error)
			throws IOException {
		if (spellChecker.exist(sentance)) {
			return;
		} else if (sentance.length()<8) {
			error++;
			getSplit(sentance, sequence, 0, error);
		}
		else {
			for (int walkStep = step; walkStep < 8; walkStep++) {
				String candidateNGram = sentance.substring(0, walkStep);
				if (spellChecker.exist(candidateNGram)) {
					sequence.put(candidateNGram, 0);
					sentance = sentance.replaceFirst(candidateNGram, "");
					getSplit(sentance, sequence, 0, error);
				} else {
					getSplit(sentance, sequence, (step + 1), error);
				}
			}
		}

	}

}

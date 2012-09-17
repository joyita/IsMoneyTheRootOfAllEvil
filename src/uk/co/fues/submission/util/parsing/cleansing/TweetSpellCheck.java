package uk.co.fues.submission.util.parsing.cleansing;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.LevensteinDistance;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.tokenizers.WordTokenizer;

public class TweetSpellCheck {

	Logger log = LoggerFactory.getLogger(getClass());
	SpellChecker spellChecker;
	WordTokenizer tokensier;

	public TweetSpellCheck() {
		try {
			tokensier = new WordTokenizer();
			tokensier.setDelimiters(" \\r\\n\\t.,;:\"()?!");
			spellChecker = new SpellChecker(new RAMDirectory(), new LevensteinDistance());
			@SuppressWarnings("deprecation")
			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_20, null);
			spellChecker
					.indexDictionary(
							new PlainTextDictionary(
									new File(
											"/workspace/Money Is The Root Of All Evil/resources/dictionary/fulldictionary00.txt")),
							config, true);
		} catch (IOException e) {
			log.error("", e);
		}

	}

	public String spellcheck(String checkme) {

		StringBuilder builder = new StringBuilder();
		tokensier.tokenize(checkme);
		String ret = checkme.replaceAll("#", "");
		ret = ret.replaceAll("@", "");
		
		while (tokensier.hasMoreElements()) {
			String word = (String) tokensier.nextElement();
			try {

				if (spellChecker.exist(word)) {
					builder.append(word + " ");
				} else {
					String[] suggestions = spellChecker.suggestSimilar(checkme,
							1);
					if (suggestions != null && suggestions.length > 0
							&& !word.contains("'")) {
						builder.append(suggestions[0] + " ");
						ret = ret.replaceFirst("word", suggestions[0]);
					} else {
						builder.append(word + " ");
					}
				}
			} catch (IOException e) {
				log.error("", e);
			}
		}

		return ret;

	}

}
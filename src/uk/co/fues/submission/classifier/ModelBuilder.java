package uk.co.fues.submission.classifier;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import uk.co.fues.submission.classifier.nlp.PhraseExtractor;
import uk.co.fues.submission.scheduled.Scheduled;
import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;

@Component
@ComponentScan("uk.co.fues.submission.classifier")
public class ModelBuilder implements Scheduled {

	PhraseExtractor extractor;
	Logger log = LoggerFactory.getLogger(getClass());
	
	public ModelBuilder() {
		extractor = new PhraseExtractor();
	}
	
	public List<String> extractPhraseVocab(String text) {
		return extractor.extractPhrases(text);
	}
	
	@Async
	public void job() {
		for (Sins si : Sins.values()) {
			String sin = si.name();
			File dir = new File(Directories.SIN_UNPARSED_DIRECTORY.getLocation() + "/"
					+ sin);
			for (File f : dir.listFiles()) {
				String data;
				try {
					data = FileUtils.readFileToString(f);
					FileUtils.writeLines(new File(Directories.SIN_DIRECTORY.getLocation() + "/" + si.name() + "/" + f.getName()), extractPhraseVocab(data), true);
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
	}
}

package uk.co.fues.submission.classifier.nlp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import uk.co.fues.submission.util.parsing.RegexFunctions;

public class PhraseExtractor {

	Logger log = LoggerFactory.getLogger(getClass());
	OpenNLPWrapper opennlp;
	
	public PhraseExtractor() {
		opennlp = new OpenNLPWrapper();
	}
	
	public List<String> extractPhrases(String text) {
		
		List<Parse> parses = getParseTree(getSentances(text));
		List<String> phrases = new ArrayList<String>();
		for (Parse parse : parses) {
			recurseParses(parse, phrases);
		}
		log.debug(phrases.toString());
		return phrases;
	}
	
	
	public List<Parse> getParseTree(String[] sentances) {
		List<Parse> parses = new ArrayList<Parse>();
		for (String sentance : sentances) {
			parses.add(ParserTool.parseLine(sentance, /*(Parser) repo.getResource(ResourceType.PARSER)*/ opennlp.getParser(), 1)[0]);
		}
		return parses;
	}
	
	
	public String[] getSentances(String document) {
		return opennlp.getSentenceDetector().sentDetect(document);
	}

	public void recurseParses(Parse parse, List<String> chosenOness) {
		
		if (parse.getChildCount() > 0) {
			for (Parse child : parse.getChildren()) {
					String phrase = extractNounPhrase(child);
					if(isPhrase(child.getType())) {
						phrase = normaliseText(phrase);
						if(phrase.length()>3) {
							for(String word:phrase.split(" ")) {
								chosenOness.add(word);															
							}
						}
					}
				recurseParses(child, chosenOness);
			}
		} else {
				String phrase = extractNounPhrase(parse);
				if(isPhrase(parse.getType())) {
					phrase = normaliseText(phrase);
					if(phrase.length()>3) {
						for(String word:phrase.split(" ")) {
							chosenOness.add(word);															
						}
					}
				}
		}
	}
	
	public boolean isPhrase(String tag) {
		if(tag.equalsIgnoreCase("NP")) {
			return true;
		}
		if(tag.equalsIgnoreCase("VP")) {
			return true;
		}
		if(tag.equalsIgnoreCase("JJ")) {
			return true;
		}
		return false;
	}

	private String extractNounPhrase(Parse child) {
		return child.getText().substring(child.getSpan().getStart(),
				child.getSpan().getEnd());
	}
	
	StringBuilder stripped = new StringBuilder();
	private String normaliseText(String text) {
		stripped.delete(0, stripped.length());
		String [] words = text.split(" ");
		for(String word:words) {
			word = word.replaceAll(RegexFunctions.removeNonAlphabeticCharacters(), "").toLowerCase();
			stripped.append(word + " ");
		}
		return stripped.toString();
	}
}

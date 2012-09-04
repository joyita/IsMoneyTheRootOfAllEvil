package uk.co.fues.submission.classifier.nlp;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OpenNLPWrapper {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	POSTaggerME tagger;
	Parser parser;
	Chunker chuncker;
	SentenceDetectorME sentenceDetector;
	
	public OpenNLPWrapper() {
		InputStream POSmodelIn = null;
		POSModel posmodel = null;

		try {
			POSmodelIn = this.getClass().getResourceAsStream("/en-pos-maxent.bin");
			posmodel = new POSModel(POSmodelIn);
			tagger = new POSTaggerME(posmodel);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			if (POSmodelIn != null) {
				try {
					POSmodelIn.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		
		InputStream modelIn = null;
		try {
			modelIn = this.getClass().getResourceAsStream("/en-parser-chunking.bin");
			ParserModel model = new ParserModel(modelIn);
			parser = ParserFactory.create(model);
			
		} catch (IOException e) {
			logger.error("", e);
		}

		finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

		InputStream modelIn2 = null;
		ChunkerModel model = null;

		try {
			modelIn2 = this.getClass().getResourceAsStream("/en-chunker.bin");
			model = new ChunkerModel(modelIn2);
			chuncker = new ChunkerME(model);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

		InputStream sentenceModelIn;

		try {
			sentenceModelIn = this.getClass().getResourceAsStream("/en-sent.bin");
			SentenceModel smodel = new SentenceModel(sentenceModelIn);
			sentenceDetector = new SentenceDetectorME(smodel);
		} catch (IOException e) {
			logger.error("", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		
		
	}

	public POSTaggerME getTagger() {
		return tagger;
	}

	public Parser getParser() {
		return parser;
	}

	public Chunker getChuncker() {
		return chuncker;
	}

	public SentenceDetectorME getSentenceDetector() {
		return sentenceDetector;
	}
}

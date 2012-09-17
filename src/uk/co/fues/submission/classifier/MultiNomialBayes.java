package uk.co.fues.submission.classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.fues.submission.classifier.nlp.PhraseTokeniser;
import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MultiNomialBayes {

	Logger log = LoggerFactory.getLogger(getClass());

	private static final String FILENAME = "data/filteredData.arff";

	Map<String, Integer> vocab;

	private StringToWordVector filter;
	private Classifier classifier;
	private FastVector classValues;
	private FastVector attributes;
	private Instances data;

	public MultiNomialBayes() throws FileNotFoundException {
		filter = new StringToWordVector();
		filter.setStopwords(new File("en.txt"));
		buildTrainingData();
	}
    
    private boolean loadClassifier() {
		try {
			buildTrainingData();
			log.debug("loading classifier");
			classifier = new NaiveBayesMultinomialUpdateable();
		    BufferedReader reader = new BufferedReader(
	                new FileReader(FILENAME));
		    data = new Instances(reader);
		    data.setClassIndex(1);
		    reader.close();
            filter.setInputFormat(data);

            Instances filteredData = Filter.useFilter(data, filter);

			classifier.buildClassifier(filteredData);
		} catch (Exception e) {
		    log.error("", e);
			return false;
		}
	    return false;
    }
	
	public void buildTrainingData() {
		this.attributes = new FastVector(2);
		this.attributes.addElement(new Attribute("___text___", (FastVector) null));
		this.classValues = new FastVector(8);

		for (Sins sin : Sins.values()) {
			classValues.addElement(sin.getClassName());
 		}

		attributes.addElement(new Attribute("___class___", classValues));
		Instances trainingData = new Instances("SiteClassifier",
				attributes, 3000);
		trainingData.setClassIndex(1);

		try {
//			int quickLimit = 0;

			for (Sins si : Sins.values()) {
				String sin = si.name();
				File dir = new File(Directories.SIN_DIRECTORY.getLocation() + "/"
						+ sin);
				for (File f : dir.listFiles()) {
					String data = FileUtils.readFileToString(f);
					addData(trainingData, data, si.getClassName());
//					quickLimit++;
//					if(quickLimit>2)
//					break;
				}
//				break;
			}
		    
		    ArffSaver saver = new ArffSaver();
		    saver.setInstances(trainingData);
		    saver.setFile(new File(FILENAME));
		    saver.setDestination(new File(FILENAME));   // **not** necessary in 3.5.4 and later
		    saver.writeBatch();

		} catch (IOException e) {
			log.error("", e);
		} catch (Exception e) {
			log.error("", e);
		}		
	}

	public void addData(Instances trainingData, String message, String classValue) throws IOException {

        message = message.toLowerCase();
        classValue = classValue.toLowerCase();
        // Make message into instance.
        Instance instance = getInstance(message, trainingData);
        // Set class value for instance.
        instance.setClassValue(classValue);
        // Add instance to training data.
        trainingData.add(instance);
		

	}

	public double[] classifySite(String text) throws Exception {
    	if(classifier==null) {
    		loadClassifier();
    	}
		
		text = text.toLowerCase();


		Instances testset = data.stringFreeStructure();
		Instance testInstance = getInstance(text, testset);
		filter.setTokenizer(new PhraseTokeniser());

		filter.input(testInstance);
		filter.batchFinished();
		Instance filteredInstance = filter.output();
		double[] retval = classifier.distributionForInstance(filteredInstance);
		for (double d : retval) {
			log.debug(d + "  ");
		}
		log.debug("~");
		return retval;

	}

	private Instance getInstance(String text, Instances data) {
		// Create instance of length two.
		Instance instance = new Instance(2);
		// Set value for message attribute
		Attribute messageAtt = data.attribute("___text___");
		instance.setValue(messageAtt, messageAtt.addStringValue(text));
		// Give instance access to attribute information from the dataset.
		instance.setDataset(data);
		return instance;
	}

}

package uk.co.fues.submission.classifier;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import uk.co.fues.submission.classifier.nlp.PhraseTokeniser;
import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MultiNomialBayes {

	private static final String FILENAME = "/MultiNomialBayes.model";

	Map<String, Integer> vocab;

	private Instances trainingData;
	private StringToWordVector filter;
	private Classifier classifier;
	private FastVector classValues;
	private FastVector attributes;

	private Instances filteredData;
	private Instances header;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public MultiNomialBayes() throws FileNotFoundException {
	}
    
    private boolean loadmodel() {
	    Vector v;
		try {
			v = (Vector) SerializationHelper.read(FILENAME);
		    classifier = (Classifier) v.get(0);
		    header = (Instances) v.get(1);
		    if(classifier!=null) {
		    	return true;
		    }
		} catch (Exception e) {
		    return false;
		}
	    return false;
    }
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildClassifier() {
		this.filter = new StringToWordVector();
		filter.setStopwords(new File("en.txt"));
		filter.setTokenizer(new PhraseTokeniser());
		this.classifier = new NaiveBayesMultinomialUpdateable();
		this.classifier = new NaiveBayesMultinomialUpdateable();
		// Create vector of attributes.
		this.attributes = new FastVector(2);
		// Add attribute for holding texts.
		this.attributes.addElement(new Attribute("text", (FastVector) null));
		// Add class attribute.
		this.classValues = new FastVector(8);

		for (Sins sin : Sins.values()) {
			classValues.addElement(sin.name());
			System.out.println(sin.name());
		}

		attributes.addElement(new Attribute("__class__", classValues));
		// Create dataset with initial capacity of 100, and set index of class.
		trainingData = new Instances("MessageClassificationProblem",
				attributes, 200);
		trainingData.setClassIndex(trainingData.numAttributes() - 1);

		try {
			addData();
			filter.setInputFormat(trainingData);
			// Generate word counts from the training data.
			filteredData = Filter.useFilter(trainingData, filter);
			// save classifier.
			classifier.buildClassifier(filteredData);
			Vector v = new Vector();
		    v.add(classifier);
		    v.add(new Instances(filteredData, 0));
		    SerializationHelper.write(FILENAME, v);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void addData() throws IOException {

		for (Sins si : Sins.values()) {
			String sin = si.name();
			File dir = new File(Directories.SIN_DIRECTORY.getLocation() + "/"
					+ sin);
			for (File f : dir.listFiles()) {
				String data = FileUtils.readFileToString(f);
				Instance instance = getInstance(data, trainingData);
				instance.setClassValue(sin);
				trainingData.add(instance);
				return;
			}
		}

	}

	public double[] classifyMessage(String message) throws Exception {
		if(!loadmodel()) {
			buildClassifier();
		}
		message = message.toLowerCase();

		// Check whether classifier has been built.
		if (trainingData.numInstances() == 0) {
			throw new Exception("No classifier available.");
		}
		Instances testset = trainingData.stringFreeStructure();
		Instance testInstance = getInstance(message, testset);

		// Filter instance.
		// filter.setInputFormat(testset);
		filter.input(testInstance);
		Instance filteredInstance = filter.output();
		double[] retval = classifier.distributionForInstance(filteredInstance);
		for (double d : retval) {
			System.out.print(d + "  ");
		}
		System.out.println("--");
		return retval;

	}

	private Instance getInstance(String text, Instances data) {
		// Create instance of length two.
		Instance instance = new Instance(2);
		// Set value for message attribute
		Attribute messageAtt = data.attribute("text");
		instance.setValue(messageAtt, messageAtt.addStringValue(text));
		// Give instance access to attribute information from the dataset.
		instance.setDataset(data);
		return instance;
	}

	static String a = "";
}

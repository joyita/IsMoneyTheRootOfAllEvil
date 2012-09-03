package uk.co.fues.submission.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MultiNomialBayes {
	
	Map<String, Integer> vocab;

    private Instances trainingData;
    private StringToWordVector filter;
    private Classifier classifier;
    private FastVector classValues;
    private FastVector attributes;

    private Instances filteredData;
    
    public MultiNomialBayes() throws FileNotFoundException {
        this.filter = new StringToWordVector();
        this.classifier = new NaiveBayesMultinomialUpdateable();
        // Create vector of attributes.
        this.attributes = new FastVector(2);
        // Add attribute for holding texts.
        this.attributes.addElement(new Attribute("text", (FastVector) null));
        // Add class attribute.
        this.classValues = new FastVector(8);
        
		for(Sins sin:Sins.values()) {
	        classValues.addElement(sin.name());
	        System.out.println(sin.name());
		}
		
        attributes.addElement(new Attribute("__class__", classValues));
        // Create dataset with initial capacity of 100, and set index of class.
        trainingData = new Instances("MessageClassificationProblem", attributes, 200);
        trainingData.setClassIndex(trainingData.numAttributes() - 1);

        try {
			addData();
            filter.setInputFormat(trainingData);
            // Generate word counts from the training data.
            filteredData = Filter.useFilter(trainingData, filter);
            // Rebuild classifier.
            classifier.buildClassifier(filteredData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    public void addData() throws IOException {

		for(Sins si:Sins.values()) {
			String sin = si.name();
			File dir = new File(Directories.SIN_DIRECTORY.getLocation() + "/" + sin);
			for(File f : dir.listFiles()) {
				String data = FileUtils.readFileToString(f);
		        Instance instance = getInstance(data, trainingData);
		        instance.setClassValue(sin);
		        trainingData.add(instance);				
			}
		}
  
    }


    public double[] classifyMessage(String message) throws Exception {
        message = message.toLowerCase();

        // Check whether classifier has been built.
        if (trainingData.numInstances() == 0) {
            throw new Exception("No classifier available.");
        }
        Instances testset = trainingData.stringFreeStructure();
        Instance testInstance = getInstance(message, testset);

        // Filter instance.
        filter.input(testInstance);
        Instance filteredInstance = filter.output();
        return classifier.distributionForInstance(filteredInstance);

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

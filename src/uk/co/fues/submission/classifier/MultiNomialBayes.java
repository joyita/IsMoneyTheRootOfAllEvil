package uk.co.fues.submission.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import uk.co.fues.submission.util.constants.Directories;
import uk.co.fues.submission.vocabulary.Sins;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.bayes.net.estimate.MultiNomialBMAEstimator;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MultiNomialBayes {
	
	Map<String, Integer> vocab;

    private Instances trainingData;
    private StringToWordVector filter;
    private Classifier classifier;
    private boolean upToDate;
    private FastVector classValues;
    private FastVector attributes;
    private boolean setup;

    private Instances filteredData;
    
    private static final long serialVersionUID = -1397598966481635120L;
    public static void main(String[] args) {
        try {
        	MultiNomialBayes cl = new MultiNomialBayes();
 
            //


            double[] result = cl.classifyMessage(a);
            System.out.println("====== RESULT ====== \tCLASSIFIED AS:\t" + Arrays.toString(result));

            result = cl.classifyMessage("asdasdasd");
            System.out.println("====== RESULT ======\tCLASSIFIED AS:\t" + Arrays.toString(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
				data = data.replaceAll("\\s+", " ");
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

	public void again() {
		vocab = new HashMap<String, Integer>();
		FastVector atts = new FastVector();
	     // - numeric
		
		for(String word:vocab.keySet()) {
		     atts.addElement(new Attribute(word));			
		}
		

		Instances data = new Instances("ooo", atts, 0);
		
		
	     if (data.classIndex() == -1)
	       data.setClassIndex(data.numAttributes() - 1);
	 
	     // set weights
	     double factor = 0.5  / (double) data.numInstances();
	     for (int i = 0; i < data.numInstances(); i++) {
	       data.instance(i).setWeight(0.5 + factor*i);
	     }

	}
	public void build(Sins sin) throws Exception {
	    TextDirectoryLoader loader = new TextDirectoryLoader();
	    String dir = Directories.SIN_DIRECTORY.getLocation() + "/" + sin.name() + "/";
	    File f = new File(dir);
	   String [] d = f.list();
	    boolean ok = f.isDirectory();
	    loader.setDirectory(f);
	    Instances dataRaw = loader.getDataSet();
	    System.out.println("\n\nImported data:\n\n" + dataRaw);

	    // apply the StringToWordVector
	    // (see the source code of setOptions(String[]) method of the filter
	    // if you want to know which command-line option corresponds to which
	    // bean property)
	    StringToWordVector filter = new StringToWordVector();
	    filter.setInputFormat(dataRaw);
	    Instances dataFiltered = Filter.useFilter(dataRaw, filter);
	    System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
	    J48 classifier = new J48();
	    classifier.buildClassifier(dataFiltered);

	}
		   public static void main2(String[] args) throws Exception {
		     FastVector      atts;
		     FastVector      attsRel;
		     FastVector      attVals;
		     FastVector      attValsRel;
		     Instances       data;
		     Instances       dataRel;
		     double[]        vals;
		     double[]        valsRel;
		     int             i;
		 
		     // 1. set up attributes
		     atts = new FastVector();
		     // - numeric
		     atts.addElement(new Attribute("att1"));
		     // - nominal
		     attVals = new FastVector();
		     for (i = 0; i < 5; i++)
		       attVals.addElement("val" + (i+1));
		     atts.addElement(new Attribute("att2", attVals));
		     // - string
		     atts.addElement(new Attribute("att3", (FastVector) null));
		     // - date
		     atts.addElement(new Attribute("att4", "yyyy-MM-dd"));
		     // - relational
		     attsRel = new FastVector();
		     // -- numeric
		     attsRel.addElement(new Attribute("att5.1"));
		     // -- nominal
		     attValsRel = new FastVector();
		     for (i = 0; i < 5; i++)
		       attValsRel.addElement("val5." + (i+1));
		     attsRel.addElement(new Attribute("att5.2", attValsRel));
		     dataRel = new Instances("att5", attsRel, 0);
		     atts.addElement(new Attribute("att5", dataRel, 0));
		 
		     // 2. create Instances object
		     data = new Instances("MyRelation", atts, 0);
		 
		     // 3. fill with data
		     // first instance
		     vals = new double[data.numAttributes()];
		     // - numeric
		     vals[0] = Math.PI;
		     // - nominal
		     vals[1] = attVals.indexOf("val3");
		     // - string
		     vals[2] = data.attribute(2).addStringValue("This is a string!");
		     // - date
		     vals[3] = data.attribute(3).parseDate("2001-11-09");
		     // - relational
		     dataRel = new Instances(data.attribute(4).relation(), 0);
		     // -- first instance
		     valsRel = new double[2];
		     valsRel[0] = Math.PI + 1;
		     valsRel[1] = attValsRel.indexOf("val5.3");
		     dataRel.add(new Instance(1.0, valsRel));
		     // -- second instance
		     valsRel = new double[2];
		     valsRel[0] = Math.PI + 2;
		     valsRel[1] = attValsRel.indexOf("val5.2");
		     dataRel.add(new Instance(1.0, valsRel));
		     vals[4] = data.attribute(4).addRelation(dataRel);
		     // add
		     data.add(new Instance(1.0, vals));
		 
		     // second instance
		     vals = new double[data.numAttributes()];  // important: needs NEW array!
		     // - numeric
		     vals[0] = Math.E;
		     // - nominal
		     vals[1] = attVals.indexOf("val1");
		     // - string
		     vals[2] = data.attribute(2).addStringValue("And another one!");
		     // - date
		     vals[3] = data.attribute(3).parseDate("2000-12-01");
		     // - relational
		     dataRel = new Instances(data.attribute(4).relation(), 0);
		     // -- first instance
		     valsRel = new double[2];
		     valsRel[0] = Math.E + 1;
		     valsRel[1] = attValsRel.indexOf("val5.4");
		     dataRel.add(new Instance(1.0, valsRel));
		     // -- second instance
		     valsRel = new double[2];
		     valsRel[0] = Math.E + 2;
		     valsRel[1] = attValsRel.indexOf("val5.1");
		     dataRel.add(new Instance(1.0, valsRel));
		     vals[4] = data.attribute(4).addRelation(dataRel);
		     // add
		     data.add(new Instance(1.0, vals));
		 
		     // 4. output data
		     System.out.println(data);
		   }

	static String a = "A large number of people who encountered R D Laing (including one I've met personally) believed that he showed clear signs of serious mental illness himself. If that's true, then does it make his highly subjective approach to mental illness more valid or less? Did the insight he gained from his own experience make him better placed to help others, or did it prevent him from objectively assessing the risks associated with some of the behaviours displayed?\n" + 
			"\n" + 
			"";
}

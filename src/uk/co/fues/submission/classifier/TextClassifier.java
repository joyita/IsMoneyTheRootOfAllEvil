package uk.co.fues.submission.classifier;

import java.io.File;
import java.io.FileNotFoundException;
    import java.io.Serializable;
    import java.util.Arrays;

    import weka.classifiers.Classifier;
    import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
    import weka.core.Attribute;
    import weka.core.FastVector;
    import weka.core.Instance;
    import weka.core.Instances;
import weka.core.converters.ArffSaver;
    import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

    public class TextClassifier implements Serializable {

        private static final long serialVersionUID = -1397598966481635120L;
        public static void main(String[] args) {
            try {
                TextClassifier cl = new TextClassifier(new NaiveBayesMultinomialUpdateable());
                cl.addCategory("computer");
                cl.addCategory("sport");
                cl.addCategory("unknown");
                cl.setupAfterCategorysAdded();

                //
                cl.addData("cs so may wow", "computer");
                cl.addData("java are amazeballs", "computer");
                cl.addData("soccer go go go", "sport");
                cl.addData("snowboard whooee", "sport");

                double[] result = cl.classifyMessage("java");
                System.out.println("====== RESULT ====== \tCLASSIFIED AS:\t" + Arrays.toString(result));

                result = cl.classifyMessage("asdasdasd");
                System.out.println("====== RESULT ======\tCLASSIFIED AS:\t" + Arrays.toString(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private Instances trainingData;
        private StringToWordVector filter;
        private Classifier classifier;
        private FastVector classValues;
        private FastVector attributes;
        private boolean setup;

        private Instances filteredData;

        public TextClassifier(Classifier classifier) throws FileNotFoundException {
            this(classifier, 10);
        }

        public TextClassifier(Classifier classifier, int startSize) throws FileNotFoundException {
            this.filter = new StringToWordVector();
            this.classifier = classifier;
            // Create vector of attributes.
            this.attributes = new FastVector(2);
            // Add attribute for holding texts.
            this.attributes.addElement(new Attribute("text", (FastVector) null));
            // Add class attribute.
            this.classValues = new FastVector(startSize);
            this.setup = false;

        }

        public void addCategory(String category) {
            category = category.toLowerCase();
            // if required, double the capacity.
            int capacity = classValues.capacity();
            if (classValues.size() > (capacity - 5)) {
                classValues.setCapacity(capacity * 2);
            }
            classValues.addElement(category);
        }

        public void addData(String message, String classValue) throws IllegalStateException {
            if (!setup) {
                throw new IllegalStateException("Must use setup first");
            }
            message = message.toLowerCase();
            classValue = classValue.toLowerCase();
            // Make message into instance.
            Instance instance = makeInstance(message, trainingData);
            // Set class value for instance.
            instance.setClassValue(classValue);
            // Add instance to training data.
            trainingData.add(instance);
        }

        /**
         * Check whether classifier and filter are up to date. Build i necessary.
         * @throws Exception
         */
        private void buildIfNeeded() throws Exception {
           if (true) {
                // Initialize filter and tell it about the input format.
                filter.setInputFormat(trainingData);
                // Generate word counts from the training data.
                filteredData = Filter.useFilter(trainingData, filter);
                // Rebuild classifier.
                classifier.buildClassifier(filteredData);
    		    ArffSaver saver = new ArffSaver();
    		    saver.setInstances(filteredData);
    		    saver.setFile(new File("data/filteredData.arff"));
    		    saver.setDestination(new File("data/filteredData.arff"));   // **not** necessary in 3.5.4 and later
    		    saver.writeBatch();
            }
        }

        public double[] classifyMessage(String message) throws Exception {
            message = message.toLowerCase();
            if (!setup) {
                throw new Exception("Must use setup first");
            }
            // Check whether classifier has been built.
            if (trainingData.numInstances() == 0) {
                throw new Exception("No classifier available.");
            }
            buildIfNeeded();
            Instances testset = trainingData.stringFreeStructure();
            Instance testInstance = makeInstance(message, testset);

            // Filter instance.
            filter.input(testInstance);
            Instance filteredInstance = filter.output();
            return classifier.distributionForInstance(filteredInstance);

        }

        private Instance makeInstance(String text, Instances data) {
            // Create instance of length two.
            Instance instance = new Instance(2);
            // Set value for message attribute
            Attribute messageAtt = data.attribute("text");
            instance.setValue(messageAtt, messageAtt.addStringValue(text));
            // Give instance access to attribute information from the dataset.
            instance.setDataset(data);
            return instance;
        }

        public void setupAfterCategorysAdded() {
            attributes.addElement(new Attribute("class", classValues));
            // Create dataset with initial capacity of 100, and set index of class.
            trainingData = new Instances("MessageClassificationProblem", attributes, 100);
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            setup = true;
        }

    }

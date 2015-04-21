package com.bah.ml.classifiers;

import com.bah.ml.data.C45.C45Loader;
import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Perceptron description
 * <p>
 *     A standard perceptron for training models from C4.5 style data and testing against the created models
 * </p>
 */

public class Perceptron {
    static Logger log = Logger.getLogger(Perceptron.class.getName());

    public enum Mode {
        TRAIN("train"),
        TEST("test");

        private final String mode;

        Mode (String mode) {
            this.mode = mode;
        }
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {

        this.dataSource = dataSource;
    }

    public int getkFolds() {
        return kFolds;
    }

    public void setkFolds(int kFolds) {
        this.kFolds = kFolds;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public boolean isRandomLearning() {
        return randomLearning;
    }

    public void setRandomLearning(boolean randomLearning) {
        this.randomLearning = randomLearning;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public double getMinError() {
        return minError;
    }

    public void setMinError(double minError) {
        this.minError = minError;
    }

    private Mode mode;

    private int kFolds = 10;

    private int maxIterations;

    private double minError;

    private String dataSource;

    private List<String> targets = new ArrayList<String>();

    private boolean randomLearning = false;

    private double learningRate = 0.5;

    public Perceptron() {
    }

    public void run() {
        if (mode == Mode.TRAIN) {
            C45Loader loader = new C45Loader();
            DataCollection trainingData = loader.load(dataSource);

            train(trainingData);
        } else if (mode == Mode.TEST) {
            test();
        } else {
            log.warn("Mode was not specified");
        }
    }

    private void train(DataCollection data) {

        log.debug("Training " + data.getSize() + " data points");

        // Determine targets
        if (targets.size() == 0) {
            targets.addAll(data.getLabels().keySet());
        }

        Random rand = new Random();

        for (String target: targets) {

            double fBestError = 1.0;
            double[] bestWeights = new double[data.getDims()];
            Arrays.fill(bestWeights, 1.0);

            for (int iFold = 0; iFold < kFolds; iFold++) {
                log.debug("Training on fold: " + iFold + " of " + kFolds);

                // Split the data into testing and training
                DataCollection[] split = splitData(data, .10);

                DataCollection trainingData = split[0];
                DataCollection testData = split[1];

                // Initialization Step
                //double fFoldBias = 1.0;
                double[] foldWeights = new double[trainingData.getDims()];
                //Arrays.fill(foldWeights, rand.nextDouble());
                Arrays.fill(foldWeights, 1.0);

                int iteration = 0;
                double errorRate = 1.0;

                int iCorrect = 0;
                int iError = 0;

                // Generate the candidate solution
                while (iteration++ <= maxIterations && errorRate >= minError) {

                    double lr = (randomLearning ? rand.nextDouble() : getLearningRate());

                    for (DataPoint dataPoint : trainingData.getDataPoints()) {
                        double score = inner_product(dataPoint.getData(), foldWeights);
                        boolean bMatch = target.equals(dataPoint.getLabel());

                        // Update the weights
                        if (score > 0.0 && !bMatch) {
                            iError++;
                            //fFoldBias--;

                            for (int idx = 0; idx < trainingData.getDims(); idx++) {
                                foldWeights[idx] -= lr * dataPoint.getData()[idx];
                            }
                        } else if (score <= 0 && bMatch) {
                            iError++;
                            //fFoldBias++;

                            for (int idx = 0; idx < trainingData.getDims(); idx++) {
                                foldWeights[idx] += lr * dataPoint.getData()[idx];
                            }
                        } else {
                            iCorrect++;
                        }
                    }

                    // Calculate the error rate
                    errorRate = (double)iError / (iError + iCorrect);
                }

                // Test the candidate solution
                int falsePositive = 0;
                int falseNegative = 0;
                int truePositive = 0;
                int trueNegative = 0;

                for (DataPoint dataPoint : testData.getDataPoints()) {
                    double testScore = fitness(dataPoint, foldWeights);
                    boolean bClassMatch = target.equals(dataPoint.getLabel());

                    if (testScore > 0 && bClassMatch) {
                        truePositive++;
                    } else if (testScore > 0 && !bClassMatch) {
                        falsePositive++;
                    } else if (testScore <= 0 && bClassMatch) {
                        falseNegative++;
                    } else if (testScore <= 0 && !bClassMatch) {
                        falsePositive++;
                    }
                }

                double foldError = (double)(truePositive + trueNegative) / (testData.getSize());

                System.out.println("Fold Error: " + foldError);
                System.out.println(Arrays.toString(foldWeights));

                if (foldError < fBestError) {
                    fBestError = foldError;
                    bestWeights = Arrays.copyOf(foldWeights, foldWeights.length);
                    System.out.println("New Best");
                }
                System.out.println();
            }
        }
    }

    private double fitness(DataPoint dataPoint, double[] solution) {
        return inner_product(dataPoint.getData(), solution);
    }

    /**
     * Randomly splits the contents of a DataCollection into 2 new DataCollections
     * @param dataCollection Input
     * @param percentage Percent of input values that are placed into the 0th data collection
     *                   The value must be between 0.0 and 1.0
     * @return
     */
    private DataCollection[] splitData(DataCollection dataCollection, double percentage) {
        DataCollection[] data = new DataCollection[2];
        data[0] = new DataCollection();
        data[1] = new DataCollection();

        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int idx = 0; idx < dataCollection.getSize(); idx++) {
            indexes.add(idx);
        }
        Collections.shuffle(indexes);

        int iLimit = (int)(percentage * dataCollection.getSize());

        // Populate the first DataCollection
        for (int idx = 0; idx < iLimit; idx++) {
            int dataIdx = indexes.get(idx);
            data[0].addDataPoint(dataCollection.getDataPoints().get(dataIdx));
        }

        // Populate the second DataCollection
        for (int idx = iLimit; idx < dataCollection.getSize(); idx++) {
            int dataIdx = indexes.get(idx);
            data[1].addDataPoint(dataCollection.getDataPoints().get(dataIdx));
        }

        return data;
    };


    private double inner_product(double[] d1, double[] d2, double initial) throws InvalidParameterException
    {
        if (d1.length != d2.length) {
            throw new InvalidParameterException("The arrays are not the same length");
        }
        
        double val = initial;
        for (int i = 0; i < d1.length; i++) {
            val += d1[i] * d2[i];
        }
        
        return val;
    }

    private double inner_product(double[] d1, double[] d2)
    {
        return inner_product(d1, d2, 0.0);
    }

    private void test() {
        log.debug("testing");
    }

}
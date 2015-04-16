package com.bah.ml.classifiers;

import com.bah.ml.data.C45.C45Loader;
import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;
import org.apache.log4j.Logger;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

    private void train(DataCollection trainingData) {
        log.debug("Training " + trainingData.getSize() + " data points");
        
        if (targets.size() == 0) {
            targets.addAll(trainingData.getLabels().keySet());
        }
        
        for (String target: targets) {

            // Initialization Step
            Double foldBias = -1.0;
            Random rand = new Random();

            double[] weights = new double[trainingData.getDims()];
            Arrays.fill(weights, rand.nextDouble());
            
            int iteration = 0;
            double errorRate = 1.0;

            while (iteration++ <= maxIterations && errorRate >= minError) {

                double[] calcWeights = new double[trainingData.getDims()];
                Arrays.fill(calcWeights, 0.0);
                
                double value = 0.0;
                
                for (DataPoint dataPoint : trainingData.getDataPoints()) {
                    value += inner_product(dataPoint.getData(), weights);
                }
            }
        }
    }

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
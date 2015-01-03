package com.bah.ml.classifiers;

import com.bah.ml.data.DataCollection;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

  public String getDataFile() {
    return dataFile;
  }

  public void setDataFile(String dataFile) {
    this.dataFile = dataFile;
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

  private String dataFile;

  private List<String> targets;

  private boolean randomLearning = false;

  private double learningRate = 0.5;

  public Perceptron() {

  }

  public void run() {
    if (mode == Mode.TRAIN) {
      DataCollection trainingData = new DataCollection(dataFile);

      train(trainingData);
    } else if (mode == Mode.TEST) {
      test();
    } else {
      log.warn("Mode was not specified");
    }
  }

  private void train(DataCollection trainingData) {
    log.debug("Training " + trainingData.getSize() + " data points");

    Double foldBias = 1.0;
    BigDecimal foldWeights[] = new BigDecimal[trainingData.getDims()];
    Arrays.fill(foldWeights, 1.0);


    int iteration = 0;
    double errorRate = 1.0;
    while (iteration <= maxIterations && errorRate >= minError) {



    }


  }

  private void test() {
    log.debug("testing");
  }

}
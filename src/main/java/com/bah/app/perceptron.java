package com.bah.app;

import com.bah.ml.classifiers.Perceptron;
import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class perceptron {
    private static final Logger log = Logger.getLogger(perceptron.class.getName());

    public static void main(String[] args) {
        // Configure log4j
        BasicConfigurator.configure();

        final Options options = new Options();
        options.addOption("h", "help", false, "show help");
        options.addOption("m", "mode", true, "Sets the mode <test|train>");
        options.addOption("d", "data", true, "Sets the data source (file or URL)");
        options.addOption("k", "k-folds", true, "Sets the k-folds");
        options.addOption("l", "learn-rate", true, "Sets the learning rate");
        options.addOption("r", "learn-random", false, "Will use random value for learning rate after each iteration");
        Option targetsOption = new Option("t", "target", true, "Specify which target to train/test, if not set all targets found will be trained/tested (max 256)");
        targetsOption.setArgs(256);
        options.addOption(targetsOption);
        options.addOption("i", "iterations", false, "Specify Max iterations to run for training, defaults to 1000");
        options.addOption("i", "min-error", false, "Specify minimum error while training, defaults to 0.05");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();

                formatter.printHelp("perceptron", options);
                System.exit(0);
            }

            if (cmd.hasOption("m")) {
                log.log(Level.INFO, "Using cli argument -m=" + cmd.getOptionValue("m"));
                Perceptron perceptron = new Perceptron();
                if (cmd.getOptionValue("m").toLowerCase().equals("train")) {
                    perceptron.setMode(Perceptron.Mode.TRAIN);
                    perceptron.setDataSource(cmd.getOptionValue("d"));
                    perceptron.setTargets(Arrays.asList(cmd.getOptionValues("t")));

                    perceptron.setkFolds(Integer.valueOf(cmd.getOptionValue("k", "10")));
                    if (cmd.hasOption("r")) {
                        perceptron.setRandomLearning(true);
                    } else {
                        perceptron.setRandomLearning(false);
                        perceptron.setLearningRate(Double.valueOf(cmd.getOptionValue("l", "0.5")));
                    }

                    perceptron.setMaxIterations(Integer.valueOf(cmd.getOptionValue("i", "1000")));

                    perceptron.setMinError(Double.valueOf(cmd.getOptionValue("e", "0.05")));

                    perceptron.run();
                } else {
                    perceptron.setMode(Perceptron.Mode.TEST);
                }
            } else {
                log.log(Level.SEVERE, "Missing m option");
                HelpFormatter formatter = new HelpFormatter();

                formatter.printHelp("Main", options);
                System.exit(0);
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            HelpFormatter formatter = new HelpFormatter();

            formatter.printHelp("Main", options);
            System.exit(0);
        }

    }
}
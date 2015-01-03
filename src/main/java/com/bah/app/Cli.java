package com.bah.app;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bah.ml.classifiers.Perceptron;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {
    private static final Logger log = Logger.getLogger(Cli.class.getName());

    private String[] args = null;
    private Options options = new Options();

    public Cli(String[] args) {

        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("m", "mode", true, "Sets the mode");
        options.addOption("d", "data", true, "Sets the data file");
        options.addOption("k", "k-folds", true, "Sets the k-folds");
        options.addOption("l", "learn-rate", true, "Sets the learning rate");
        options.addOption("r", "learn-random", false, "Will use random value for learning rate after each iteration");
        options.addOption("t", "target", true, "Specify which target to train, if not set all targets found will be trained");
        options.addOption("i", "iterations", false, "Specify Max iterations to run for training, defaults to 1000");
        options.addOption("i", "min-error", false, "Specify minimum error while training, defaults to 0.02");
    }

    public void parse() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h"))
                help();

            if (cmd.hasOption("m")) {
                log.log(Level.INFO, "Using cli argument -m=" + cmd.getOptionValue("m"));
                Perceptron perceptron = new Perceptron();
                if (cmd.getOptionValue("m").toLowerCase().equals("train")) {
                    perceptron.setMode(Perceptron.Mode.TRAIN);
                    perceptron.setDataFile(cmd.getOptionValue("d"));
                    perceptron.setkFolds(Integer.valueOf(cmd.getOptionValue("k", "10")));
                    if (cmd.hasOption("r")) {
                        perceptron.setRandomLearning(true);
                    } else {
                        perceptron.setRandomLearning(false);
                        perceptron.setLearningRate(Double.valueOf(cmd.getOptionValue("l", "0.5")));
                    }

                    perceptron.setMaxIterations(Integer.valueOf(cmd.getOptionValue("i", "1000")));

                    perceptron.setMinError(Double.valueOf(cmd.getOptionValue("e", "0.02")));

                    perceptron.run();
                } else {
                    perceptron.setMode(Perceptron.Mode.TEST);
                }


            } else {
                log.log(Level.SEVERE, "Missing m option");
                help();
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Main", options);
        System.exit(0);
    }
}
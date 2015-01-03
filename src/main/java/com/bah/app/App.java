package com.bah.app;

import org.apache.log4j.BasicConfigurator;

public class App {
  public static void main(String[] args) {
    // Configure log4j
    BasicConfigurator.configure();

    // Read our command line args
    new Cli(args).parse();


  }
}

/*
#include <cstdio>
#include <iostream>
#include <fstream>
#include <cstdlib>
#include <set>
#include <ctime>
//----------------------------------------------------------------------------
#include "Perceptron.h"
#include "C45Reader.h"
//----------------------------------------------------------------------------
using namespace std;
//----------------------------------------------------------------------------'
void printUsage();
//----------------------------------------------------------------------------
int main(int argc, char** argv)
{
  srand((unsigned int)time(NULL));

  string dataFile;
  string modelFile;
  set<string> vTargets;
  int iFolds = 10;
  bool bRandomLearner = true;
  float fLearningRate = 0.5;

  bool bTrain = false;
  bool bTest = false;

  for (int iArg = 0; iArg < argc; iArg++) {
    if ((iArg + 1) < argc) {
      if (strcmp(argv[iArg], "-train") == 0) {
        bTrain = true;
      } else if (strcmp(argv[iArg], "-test") == 0) {
        bTest = true;
      } else if (strcmp(argv[iArg], "-f") == 0) {
        dataFile = argv[iArg + 1];
      } else if (strcmp(argv[iArg], "-k") == 0) {
        iFolds = atoi(argv[iArg + 1]);
      } else if (strcmp(argv[iArg], "-t") == 0) {
        string sTarget = argv[iArg + 1];
        vTargets.insert(sTarget);
      } else if (strcmp(argv[iArg], "-o") == 0) {
        modelFile = argv[iArg + 1];
      } else if (strcmp(argv[iArg], "-r") == 0) {
        bRandomLearner = (bool)atoi(argv[iArg]);
        } else if (strcmp(argv[iArg], "-lr") == 0) {
        fLearningRate = atof(argv[iArg + 1]);
      } else if (strcmp(argv[iArg], "-m") == 0) {
        modelFile = argv[iArg + 1];
      }
    }
  }

  if (!bTrain && !bTest) {
    printUsage();
    exit(-1);
  }

  if (bTrain) {
    if (dataFile.empty()) {
      cerr << "No file specified\n";
      printUsage();
      exit(-1);
    }

    if (vTargets.empty()) {
      // Train all targets found
      C45Reader* reader = new C45Reader();
      vector<C45Data> data = reader->importData(dataFile);

      vector<C45Data>::iterator it = data.begin();
      while (it != data.end()) {
        vTargets.insert((*it).label);
        it++;
      }
    }

    set<string>::iterator targetIT = vTargets.begin();
    while (targetIT != vTargets.end()) {
      cout << "Training: " << (*targetIT) << endl;

      Perceptron* p = new Perceptron();
      p->importData(dataFile);
      p->init();
      p->setFolds(iFolds);
      if (bRandomLearner) {
        p->setRandomLearner(true);
      } else {
       } else {
        p->setLearningRate(fLearningRate);
      }

      p->setTarget((*targetIT));
      p->train();

      if (!modelFile.empty()) {
        ofstream modelStream;
        modelStream.open(modelFile.c_str(), ios::app);
        for (int i = 0; i < p->model().size(); i++) {
          modelStream << p->model()[i] << ",";
        }
        modelStream << (*targetIT) ;
      } else {

        for (int i = 0; i < p->model().size(); i++) {
          cout << p->model()[i] << ",";
        }
        cout << (*targetIT) << endl;
      }

      delete p;

      targetIT++;
    }
  } else {
    // Test with a model file against unknown
    if (modelFile.empty()) {
      cerr << "Missing model file\n";
      printUsage();
      exit(-1);
    }

    if (dataFile.empty()) {
      cerr << "Missing input file\n";
      printUsage();
      exit(-1);
    }

    Perceptron* p = new Perceptron();

    C45Reader* modelReader = new C45Reader();
    C45Reader* dataReader = new C45Reader();
 vector<C45Data> model = modelReader->importData(modelFile);
    vector<C45Data> targets = dataReader->importData(dataFile);

    delete modelReader;
    delete dataReader;

    vector<C45Data>::iterator targetIT = targets.begin();
    while (targetIT != targets.end()) {
      if ((*targetIT).label.empty()) {
        targetIT++;
        continue;
      }
      cout << "Matching: " << (*targetIT).label << endl;
      vector<C45Data>::iterator modelIT = model.begin();
      int iMatch = 0;
      while (modelIT != model.end()) {
        float fScore;
        if ((*modelIT).label.empty()) {
          modelIT++;
          continue;
        }
        if (p->score((*targetIT).features, (*modelIT).features, fScore)) {
          cout << "Matched " << (*targetIT).label << " on " << (*modelIT).label << " with score: " << fScore << endl;
          iMatch++;
        }
        modelIT++;
      }
      if (!iMatch) {
        cout << "No matches found" << endl;
      }
      cout << endl;
      targetIT++;
    }
  }

  return 0;
}
//----------------------------------------------------------------------------
void printUsage()
{
  cout << "Perceptron [-train|-test] -f inputFile [-k folds] [-t target] [-r|-lr rate] [-o outputFile] " << endl;
  cout << endl;
  cout << "\t-train" << endl;
  cout << "\t\tCreate a model for a target or set of targets" << endl << endl;
  cout << "\t\t-f  Training data" << endl;
  cout << "\t\t-k  Number of folds, defaults to 10" << endl;
  cout << "\t\t-t  Target to train, if set only that target will be trained" << endl;
  cout << "\t\t-r  Enable random learning during the training process" << endl;
  cout << "\t\t-lr Learning rate, sets the learning rate, if set the -r flag will be unset, defaults to 0.5" << endl;
  cout << "\t\t-o  Output file, location to save the output to, if not set output is set to stdout"<< endl << endl;
  cout << "\t-test" << endl;
  cout << endl;
  cout << "\t\tScore an input file against a model to get a MATCH/NO MATCH" << endl;
  cout << "\t\t-f  Data file" << endl;
  cout << "\t\t-m  Model file to use to score against" << endl;
}

 */

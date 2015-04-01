package com.bah.app;

import com.bah.ml.cluster.Cluster;
import com.bah.ml.cluster.KMeans;
import com.bah.ml.data.C45.C45Loader;
import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.cli.*;
import org.apache.log4j.BasicConfigurator;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 573996 on 3/24/2015.
 */
public class kmeans {
        private static final Logger log = Logger.getLogger(kmeans.class.getName());

    public static void main(String[] args) {
        // Configure log4j
        BasicConfigurator.configure();

        final Options options = new Options();
        options.addOption("h", "help", false, "show help.");
        options.addOption("d", "data", true, "Sets the data source (file or URL)");
        options.addOption("k", "clusters", true, "Sets the number of clusters");
        options.addOption("e", "exclude", true, "Exclude features by their position, use 0 based indexing using 1 and 0 as positions (eg 101 will use the first and third features), if the filter is shorter than the number of dimensions, features beyond the last specified will not be used");

        CommandLineParser parser = new BasicParser();
        CommandLine cmd = null;
        try {
            // Parse options
            cmd = parser.parse(options, args);

            // Number of Centroids
            int kcentroids = Integer.valueOf(cmd.getOptionValue("k", "3"));

            // Load the data
            C45Loader loader = new C45Loader();
            DataCollection dataSource = loader.load(cmd.getOptionValue("d"));

            if (cmd.hasOption("e")) {
                String featureFilter = cmd.getOptionValue("e");
                boolean[] bfilter = new boolean[dataSource.getDims()];

                if (!Pattern.matches("[01]+", featureFilter)) {
                    log.log(Level.SEVERE, "Feature Filter is in incorrect format");
                    System.exit(-1);
                }

                for (int cIdx = 0; cIdx < bfilter.length; cIdx++) {
                    if (cIdx > featureFilter.length()) {
                        bfilter[cIdx] = false;
                    } else {
                        bfilter[cIdx] = (featureFilter.charAt(cIdx) == '1');
                    }
                }

                Pattern dimCountPattern = Pattern.compile("1");
                Matcher dimCountMatcher = dimCountPattern.matcher(featureFilter);
                int dims = 0;
                while (dimCountMatcher.find())
                    dims++;

                DataCollection filteredData = new DataCollection();

                for (DataPoint dataPoint : dataSource.getDataPoints()) {
                    double[] data = new double[dims];
                    int filterIdx = 0;
                    for (int idx = 0; idx < dataPoint.getDimensions(); idx++) {
                        if (bfilter[idx]) {
                            data[filterIdx++] = dataPoint.getData(idx);
                        }
                    }
                    filteredData.addDataPoint(new DataPoint(data, dataPoint.getLabel()));
                }

                dataSource = filteredData;
            }

            // Initialize and run KMeans
            KMeans kMeans = new KMeans();
            kMeans.init(kcentroids, dataSource);
            kMeans.run();

            // Print the results
            for (Cluster cluster : kMeans.getClusters()) {
                System.out.println("Cluster: " + String.valueOf(cluster.getSize()));
                for (DataPoint dataPoint : cluster.getDataPoints()) {
                    System.out.println(String.valueOf(Arrays.toString(dataPoint.getData()) + dataPoint.getLabel()));
                }
            }

        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse command line properties", e);
            System.exit(0);
        }
    }
}

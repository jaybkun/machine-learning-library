package com.bah.ml.data;

import com.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 573996 on 12/23/2014.
 */
public class C45Loader {
    static Logger log = Logger.getLogger(
            C45Loader.class.getName());

    public static List<DataPoint> loadCSV(String file) {

        List<DataPoint> data = new ArrayList<DataPoint>();
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> entries = reader.readAll();

            // TODO replace with lambda expression in java 8
            if (entries.size() != 0) {
                for (String[] entry: entries) {
                    data.add(new DataPoint(entry));
                }
            }
        } catch (IOException e) {
            log.error(e);
        } finally {
            return data;
        }
    }
}

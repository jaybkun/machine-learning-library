package com.bah.ml.data.C45;

import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataLoader;
import com.bah.ml.data.DataPoint;
import com.opencsv.CSVReader;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class C45Loader implements DataLoader {
    static Logger log = Logger.getLogger(C45Loader.class.getName());

    /**
     *
     * @param dataFile
     * @return
     */
    public DataCollection load(String dataFile) {
        return load(dataFile, null);
    }

    /**
     *
     * @param dataFile
     * @param nameFile
     * @return
     */
    public DataCollection load(String dataFile, String nameFile) {
        DataCollection dataCollection = new DataCollection();

        InputStream nameSteam = null;
        if (nameFile != null) {

            try {
                URL url = new URL(nameFile);
                nameSteam = url.openStream();
            } catch (MalformedURLException urlException) {
                try {
                    nameSteam = new FileInputStream(nameFile);
                } catch (FileNotFoundException e) {
                    log.error(e);
                }
            } catch (IOException e) {
                log.error(e);
            }
        }

        InputStream dataStream = null;
        try {
            URL url = new URL(dataFile);
            dataStream = url.openStream();
        } catch (MalformedURLException urlException) {
            try {
                dataStream = new FileInputStream(dataFile);
            } catch (FileNotFoundException e) {
                log.error(e);
            }
        } catch (IOException e) {
            log.error(e);
        }

        try {
            CSVReader reader = new CSVReader(new InputStreamReader(dataStream));
            List<String[]> entries = reader.readAll();

            if (entries.size() != 0) {
                for (String[] entry: entries) {
                    boolean badEntry = false;
                    for (int idx = 0; idx < entry.length; idx++) {
                        if (entry[idx].length() == 0) {
                            badEntry = true;

                            StringBuilder builder = new StringBuilder();
                            for (String substring : entry) {
                                builder.append(substring);
                            }
                            log.warn("Bad entry in CSV, skipping [ " + builder.toString() + " ]");
                            break;
                        }
                    }
                    if (!badEntry) {
                        dataCollection.addDataPoint(new DataPoint(entry));
                    }
                }
            }
        } catch (IOException e) {
            log.error(e);
        } finally {
            return dataCollection;
        }
    }


}

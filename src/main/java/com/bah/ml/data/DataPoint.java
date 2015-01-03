package com.bah.ml.data;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

public class DataPoint {

        static Logger log = Logger.getLogger(
                DataPoint.class.getName());

        private ArrayList<BigDecimal> data;
        private final String label;

        public DataPoint(ArrayList<BigDecimal> data, String label) {
                this.data = data;
                this.label = label;
        }

        public DataPoint(String[] rawData) {
                data  = new ArrayList<BigDecimal>();
                label = new String(rawData[rawData.length - 1]);

                try {
                        DecimalFormat df = new DecimalFormat();
                        df.setParseBigDecimal(true);
                        for (int i = 0; i < (rawData.length - 1); i++) {
                                data.add((BigDecimal)df.parse(rawData[i]));
                        }

                } catch (ParseException pe) {
                        log.error(pe);
                }
        }

        public String getLabel() {
                return label;
        }

        public List<BigDecimal> getData() {
                return data;
        }

        public int getDimensions() {
                return data.size();
        }
}

package com.bah.ml.data;

import com.bah.utils.Distance;

public class DataPoint {
    public double[] getData() {
        return data;
    }

    public double getData(int idx) {
        return data[idx];
    }

    public String getLabel() {
        return label;
    }

    private final double[] data;
    private final String label;

    public enum C45_TYPES {
        continuous,
        discrete,
        ignore
    }

    /**
     * Copy Constructor
     * @param dataPoint
     */
    public DataPoint(DataPoint dataPoint) {
        this.data = dataPoint.getData();
        this.label = dataPoint.getLabel();
    }

    public DataPoint(double[] data, String label) {
        this.data = data;
        this.label = label;
    }

    public DataPoint(String[] rawData) throws RuntimeException {
        try {
            data  =  new double[rawData.length - 1];
            for (int i = 0; i < data.length; i++) {
                data[i] = Double.valueOf(rawData[i]);
            }
            label = new String(rawData[rawData.length - 1]);
        } catch (Exception e) {
            throw new RuntimeException("Encountered invalid data while creating the datapoint");
        }
    }

    public int getDimensions() {
        return data.length;
    }

    public double distanceTo(DataPoint dataPoint) {
        return Distance.euclidean(this.data, dataPoint.data);
    }
}

package com.bah.ml.data;

import com.bah.ml.data.C45.C45Loader;
import org.apache.log4j.helpers.Loader;

import java.security.InvalidParameterException;
import java.util.*;

public class DataCollection {

    private ArrayList<DataPoint> dataPoints;
    private Hashtable<String, Integer> labels;

    public DataCollection() {
        dataPoints = new ArrayList<DataPoint>();
        labels = new Hashtable<String, Integer>();
    }

    /**
     *
     * @param label
     */
    private void incLabelCount(String label) {
        int count = labels.containsKey(label) ? labels.get(label) : 0;
        labels.put(label, count + 1);
    }

    /**
     *
     * @param label
     */
    private void decLabelCount(String label) {
        if (labels.containsKey(label)) {
            int count = labels.containsKey(label) ? labels.get(label) : 0;
            if (count <= 1) {
                labels.remove(label);
            } else {
                labels.put(label, count - 1);
            }
        }
    }

    /**
     *
     * @return
     */
    public Hashtable<String, Integer> getLabels() {
        return labels;
    }

    /**
     *
     * @param label
     * @return
     */
    public int getLabelCount(String label) {
        return labels.containsKey(label) ? labels.get(label) : 0;
    }

    /**
     *
     * @param dataPoint
     * @throws InvalidParameterException
     */
    public void addDataPoint(DataPoint dataPoint) throws InvalidParameterException {
        if (dataPoints.size() == 0) {
            dataPoints.add(dataPoint);
            incLabelCount(dataPoint.getLabel());
        } else {
            if (dataPoint.getDimensions() != dataPoints.get(0).getDimensions()) {
                String error = "The dimensions of the DataPoint do not match what is already in the collection; ";
                error += "expected[" + String.valueOf(dataPoints.get(0).getDimensions()) + "] ";
                error += "got [" + String.valueOf(dataPoint.getDimensions()) + "]";

                throw new InvalidParameterException(error);
            }
            dataPoints.add(dataPoint);
            incLabelCount(dataPoint.getLabel());
        }
    }

    /**
     *
     * @param dataPoints
     */
    public void addDataPoints(List<DataPoint> dataPoints) {
        for (DataPoint dataPoint : dataPoints) {
            this.addDataPoint(dataPoint);
        }
    }

    /**
     *
     * @param dataPoint
     */
    public void removeDataPoint(DataPoint dataPoint) {
        if (dataPoints.contains(dataPoint)) {
            dataPoints.remove(dataPoint);
            decLabelCount(dataPoint.getLabel());
        }
    }

    /**
     * Returns all DataPoints in the collection
     */
    public ArrayList<DataPoint> getDataPoints() {
        return dataPoints;
    }

    /**
     * Returns all DataPoints in the collection with the specified label
     * @param label Label to filter on
     */
    public ArrayList<DataPoint> getDataPoints(String label) {
        ArrayList<DataPoint> filteredData = new ArrayList<DataPoint>();
        for (DataPoint dataPoint : dataPoints) {
            if (dataPoint.getLabel().equals(label)) {
                filteredData.add(dataPoint);
            }
        }
        return filteredData;
    }

    public int getDims() {
        return dataPoints.size() >= 1 ? dataPoints.get(0).getDimensions() : 0;
    }

    public int getSize() { return dataPoints.size(); }
}

package com.bah.ml.cluster;

import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;

import java.util.Arrays;

/**
 * Created by 573996 on 3/24/2015.
 */
public class Cluster extends DataCollection {
    private DataPoint centroid;

    public Cluster() { }

    /**
     * Copy Constructor
     * @param c
     */
    public Cluster(Cluster c) {
        this.addDataPoints(c.getDataPoints());
        this.centroid = c.getCentroid();
    }

    public DataPoint getCentroid() {
        return centroid;
    }

    public void addDataPoint(DataPoint dataPoint) {
        super.addDataPoint(dataPoint);
        updateCentroid();
    }

    public void addDataPoint(DataPoint dataPoint, boolean update) {
        super.addDataPoint(dataPoint);
        if (update) {
            updateCentroid();
        }
    }

    public void removeDataPoint(DataPoint dataPoint) {
        super.removeDataPoint(dataPoint);
        updateCentroid();
    }

    public void removeDataPoint(DataPoint dataPoint, boolean update) {
        super.removeDataPoint(dataPoint);
        if (update) {
            updateCentroid();
        }
    }

    public void updateCentroid() {
        if (getSize() == 0) {
            return;
        }

        int iDims = getDataPoints().get(0).getDimensions();
        double[] centroidData = new double[iDims];

        for (DataPoint dp : getDataPoints()) {
            for (int idx = 0; idx < iDims; idx++) {
                centroidData[idx] += dp.getData()[idx];
            }
        }

        for (int idx = 0; idx < iDims; idx++) {
            centroidData[idx] /= getSize();
        }
        centroid = new DataPoint(centroidData, "centroid");
        //System.out.println("size: "+ Integer.valueOf(getSize()) +" centroid: " + Arrays.toString(centroidData));
    }
}

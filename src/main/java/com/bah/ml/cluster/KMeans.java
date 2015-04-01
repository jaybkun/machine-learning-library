package com.bah.ml.cluster;

import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;

import java.util.*;

/**
 * K-Means clustering
 *
 * An unsupervised learning algorithm
 */

public class KMeans {
    private int iterations = 0;
    private boolean kmeansRunning = false;
    private boolean stopRequest = false;
    private boolean[] featureFilter;
    private ArrayList<Cluster> clusters = null;

    public void init(int iClusters, DataCollection data) {
        if (iClusters < 1) {
            throw new IllegalArgumentException("There must be more than 1 cluster");
        }

        clusters = new ArrayList<Cluster>();
        int dataIdx = 0;
        for (int iCluster = 0; iCluster < iClusters; iCluster++) {
            Cluster c = new Cluster();
            for (int idx = dataIdx; idx < data.getSize(); idx += iClusters) {
                c.addDataPoint(data.getDataPoints().get(idx), false);
            }
            dataIdx++;
            c.updateCentroid();
            clusters.add(c);
        }
    }

    public void run() throws RuntimeException {
        if (clusters == null) {
            throw new RuntimeException("KMeans was not initialized");
        }

        kmeansRunning = true;
        stopRequest = false;
        boolean clusterSwap = false;

        while (!clusterSwap && !stopRequest) {
            clusterSwap = true;

            HashMap<Cluster, Cluster> clusterMap = new HashMap<Cluster, Cluster>();
            for (Cluster currentCluster : clusters) {
                clusterMap.put(currentCluster, new Cluster(currentCluster));
            }

            for (Cluster currentCluster : clusters) {
                for (DataPoint dataPoint : currentCluster.getDataPoints()) {
                    double currentDist = dataPoint.distanceTo(currentCluster.getCentroid());
                    double bestDist = currentDist;
                    Cluster bestCluster = currentCluster;
                    for (Cluster comparisonCluster : clusters) {
                        double newDist = dataPoint.distanceTo(comparisonCluster.getCentroid());
                        if (newDist < bestDist) {
                            bestDist = newDist;
                            bestCluster = comparisonCluster;
                        }
                    }
                    if (bestCluster != currentCluster) {
                        clusterMap.get(currentCluster).removeDataPoint(dataPoint, false);
                        clusterMap.get(bestCluster).addDataPoint(dataPoint, false);
                        clusterSwap = false;
                    }
                }
            }

            // Replace the old clusters with the updated clusters
            clusters.removeAll(clusters);

            for (Cluster keyCluster : clusterMap.keySet()) {
                Cluster c = clusterMap.get(keyCluster);
                c.updateCentroid();
                clusters.add(c);
            }
        }

        kmeansRunning = false;
        stopRequest = false;
    }

    public ArrayList<Cluster> getClusters() {
        return clusters;
    }

    void halt() {
        if (kmeansRunning) {
            stopRequest = true;
        }
    }
}
//---------------------------------------------------------------------------


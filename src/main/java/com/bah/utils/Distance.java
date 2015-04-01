package com.bah.utils;

import java.security.InvalidParameterException;

/**
 * Created by 573996 on 3/24/2015.
 */
public class Distance {

    public static double euclidean(double[] v1, double[] v2) throws RuntimeException {
        if (v1.length != v2.length) {
            throw new RuntimeException("Arrays are not the same length");
        }

        double dist = 0.0;
        for (int idx = 0; idx < v1.length; idx++) {
            dist += Math.pow(v1[idx] - v2[idx], 2);
        }
        return Math.sqrt(dist);
    }

    public static double manhattan (double[] v1, double[] v2) {
        if (v1.length != v2.length) {
            throw new RuntimeException("Arrays are not the same length");
        }

        double dist = 0.0;
        for (int idx = 0; idx < v1.length; idx++) {
            dist += Math.abs(v1[idx] - v2[idx]);
        }
        return dist;
    }
}

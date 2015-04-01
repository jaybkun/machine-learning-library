package com.bah.ml.data;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataPointTest {

    private double[] createTestData() {
        double[] data = {1,2,3};
        return data;
    }

    private String createLabel() {
        return "datapoint";
    }

    private DataPoint createDataPoint() {
        return new DataPoint(createTestData(), createLabel());
    }

    @Test
    public void testGetLabel() throws Exception {
        DataPoint dataPoint = createDataPoint();
        assertEquals("Label was not returned correctly", createLabel(), dataPoint.getLabel());
    }

    @Test
    public void testGetData() throws Exception {
        DataPoint dataPoint = createDataPoint();
        assertArrayEquals("Data returned did not match expected", createTestData(), dataPoint.getData(), 0);
    }

    @Test
    public void testGetDimensions() throws Exception {
        DataPoint dataPoint = createDataPoint();
        assertEquals("Data is not the same dimensions", createTestData().length, dataPoint.getDimensions());
    }
}
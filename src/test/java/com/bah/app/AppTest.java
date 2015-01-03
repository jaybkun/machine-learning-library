package com.bah.app;

import com.bah.ml.data.C45Loader;
import com.bah.ml.data.DataCollection;
import com.bah.ml.data.DataPoint;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    public final String testFilePath = "C:/Users/573996/Desktop/iris.csv";


    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testC45Loader()
    {
        File dataFile = new File(testFilePath);
        assertTrue("Test data file could not be found", dataFile.exists() && !dataFile.isDirectory());

        C45Loader loader = new C45Loader();
        assertNotNull("C45Loader Constructor failed", loader);

        List<DataPoint> data = loader.loadCSV(testFilePath);
        assertTrue("Non-static C45Loader got 0 entries", data.size() != 0);

        List<DataPoint> staticLoaderData = C45Loader.loadCSV(testFilePath);
        assertTrue("Static C45Loader got 0 entries", staticLoaderData.size() != 0);

        assertEquals("Static reader produced different sized data than non-static", data.size(), staticLoaderData.size());
    }

    public void testDataCollection()
    {
        DataCollection dataCollectionEmpty = new DataCollection();
        assertNotNull(dataCollectionEmpty);

        DataCollection dataCollection = new DataCollection(testFilePath);
        assertNotNull(dataCollection);

        List<DataPoint> data = C45Loader.loadCSV(testFilePath);

        assertEquals(dataCollection.getDataPoints().size(), data.size());

        assertEquals(50, dataCollection.getDataPoints("Iris-setosa").size());
        assertEquals(50, dataCollection.getDataPoints("Iris-versicolor").size());
        assertEquals(50, dataCollection.getDataPoints("Iris-virginica").size());
    }


}

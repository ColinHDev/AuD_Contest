package com.gatdsen.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FileUtilFileCompareTest {

    //The filesystem to test in
    private static final File fs = new File("src/test/resources/FileUtilFileCompareTest/FileSystem");

    //The testset of different paths and their expected output
    private static final File config = new File("src/test/resources/FileUtilFileCompareTest/config.csv");

    /**
     * A single test sample with paths to two files and the expected output
     */
    private static class TestSample {
        public final File fileA;
        public final File fileB;
        public final boolean result;

        public TestSample(File fileA, File fileB, boolean result) {
            this.fileA = fileA;
            this.fileB = fileB;
            this.result = result;
        }
    }

    /**
     * Reads all test samples from the config file
     * @return A Collection of test samples
     * @throws IOException When the config file cannot be read
     */
    @Parameterized.Parameters
    public static Collection<TestSample> data() throws IOException {
        Assert.assertTrue("The tests resource directory (" + fs.getAbsolutePath() + ") doesn't exist.",
                fs.isDirectory());
        Assert.assertTrue("The tests configuration (" + config.getAbsolutePath() + ") doesn't exist.",
                config.exists());
        Collection<TestSample> data = new ArrayList<TestSample>();

        try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] cur = line.split(",( )*");
                Assert.assertEquals("Error reading " + config.getAbsolutePath() + " at line " + i,
                        3, cur.length);
                data.add(new TestSample(new File(fs, cur[0]), new File(fs, cur[1]), cur[2].equals("true")));
                i++;
            }
        }
        return data;
    }


    //The sample to be processed in the instances test
    private final TestSample testSample;


    public FileUtilFileCompareTest(TestSample testSample) {
        this.testSample = testSample;
    }


    /**
     * Test FileUtils.compareFiles for the File-references in the sample and validate the result
     * @throws IOException When one of the specified files cannot be read
     */
    @Test
    public void test() throws IOException {
        Assert.assertEquals("Failure for comparing " + testSample.fileA.getAbsolutePath() +
                        "\nand " + testSample.fileB.getAbsolutePath(),
                testSample.result,
                FileUtils.compareFiles(testSample.fileA, testSample.fileB));

        //FileUtils.compareFiles should be symmetrical
        Assert.assertEquals("Failure for comparing " + testSample.fileB.getAbsolutePath() +
                        "\nand " + testSample.fileA.getAbsolutePath(),
                testSample.result,
                FileUtils.compareFiles(testSample.fileB, testSample.fileA));
    }
}

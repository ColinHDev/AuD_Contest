package com.gatdsen.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@RunWith(Parameterized.class)
public class FileUtilDirContentCompareTest {

    private static final File res = new File("src/test/resources/FileUtilDirContentCompareTest");


    /**
     * Compiles a collection of directories containing test samples
     * Each sample consists of an "a" directory, a "b" directory and possibly a ".negative" file
     * The "a" and "b" directory contain the file trees that are to be compared
     * If a ".negative" file is present in the samples root the expected output is false and true otherwise
     * @return A Collection of references to all directories that contain a test-sample
     */
    @Parameterized.Parameters
    public static Collection<File> data() {
        Assert.assertTrue("The tests resource directory (" + res.getAbsolutePath() + ") doesn't exist.",
                res.isDirectory());
        return Arrays.asList(Objects.requireNonNull(res.listFiles(File::isDirectory)));
    }


    private final File testSetA;
    private final File testSetB;
    private final boolean result;

    public FileUtilDirContentCompareTest(File testSet) {
        Assert.assertTrue(testSet.isDirectory());
        testSetA = new File(testSet, "a");
        Assert.assertTrue("The tree a at " + testSetA.getAbsolutePath() + " is missing",
                testSetA.isDirectory());

        testSetB = new File(testSet, "b");
        Assert.assertTrue("The tree b at " + testSetB.getAbsolutePath() + " is missing",
                testSetB.isDirectory());

        result = !new File(testSet, ".negative").exists();
    }


    /**
     * Test FileUtils.compareContentRec for the two File-trees a and b in the sample and validate the result
     * @throws IOException When one or more files cannot be read
     */
    @Test
    public void test() throws IOException {
        Assert.assertEquals("Failure for comparing " + testSetA.getAbsolutePath() +
                        "\nand " + testSetB.getAbsolutePath(),
                result,
                FileUtils.compareContentRec(testSetA, testSetB));

        //FileUtils.compareContentRec should be symmetrical
        Assert.assertEquals("Failure for comparing " + testSetB.getAbsolutePath() +
                        "\nand " + testSetA.getAbsolutePath(),
                result,
                FileUtils.compareContentRec(testSetB, testSetA));
    }
}

package com.gats.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtilCopyClearTest {

    private static final File workingDir = new File("src/test/tmp");

    /**
     * Tests FileUtils.delDirRec by creating a simple file and an empty directory and deleting their common root
     * before restoring the root
     * @throws IOException When writing the input file fails
     */
    @Before
    public void testClear() throws IOException {
        if (!workingDir.exists() && !workingDir.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", workingDir));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(workingDir, "test.txt")));
        writer.write("lorem ipsum");
        writer.close();
        File randomDir = new File(workingDir, "dir");
        if (!randomDir.exists() && !randomDir.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", randomDir));

        FileUtils.delDirRec(workingDir);

        Assert.assertFalse("The  directory (" + workingDir.getAbsolutePath() + ") has been correctly deleted.",
                workingDir.exists());
        Assert.assertTrue("Couldn't recreate working directory " + workingDir.getAbsolutePath(),
                workingDir.mkdir());
        ;
        Assert.assertEquals("The tests working directory (" + workingDir.getAbsolutePath() + ") is not empty exist.",
                0, Objects.requireNonNull(workingDir.listFiles()).length);
    }


    /**
     * An advanced test with a large file tree
     * @throws IOException When copying of one or more files fails
     */
    @Test
    public void testCopyAndClear() throws IOException {
        //Copy a large file tree to the working dir
        Assert.assertTrue("The tests working directory (" + workingDir.getAbsolutePath() + ") doesn't exist.",
                workingDir.isDirectory());
        File src = new File("src/test/resources/FileUtilDirContentCompareTest");
        //Validate the copy process using FileUtils.compareContentRec
        FileUtils.copyContentRec(src, workingDir,  StandardCopyOption.REPLACE_EXISTING);
        Assert.assertTrue("Copying contents from " + src.getAbsolutePath() +
                        "\nto " + workingDir + "failed",
                FileUtils.compareContentRec(src, workingDir));
        //Recursively delete every subdirectory in the working dir
        for (File cur:
                Objects.requireNonNull(workingDir.listFiles(File::isDirectory))) {
            FileUtils.delDirRec(cur);
        }
        //Delete the remaining files
        for (File cur:
                Objects.requireNonNull(workingDir.listFiles(File::isFile))) {
            Assert.assertTrue("Couldn't delete file at " + cur.getAbsolutePath(),
                    cur.delete());
        }
        //The directory should be empty now
        Assert.assertEquals("Working dir at "+ workingDir.getAbsolutePath() + " contains Files after clearing.",
                0,
                Objects.requireNonNull(workingDir.listFiles()).length);
    }
}

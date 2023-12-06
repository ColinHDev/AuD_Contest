package com.gatdsen.assets;

import com.gatdsen.util.FileUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@RunWith(Parameterized.class)
public class PipelineTest {

    private static final String inputDirName = "in";
    private static final String outputDirName = "out";

    private static final File res = new File("src/test/resources/PipelineTest");

    private static final File workingDir = new File("src/test/tmp");
    private static final File inputDir = new File(workingDir, inputDirName);
    private static final File tmpDir = new File(workingDir, "tmp");
    private static final File outputDir = new File(workingDir, outputDirName);

    private static int OSType = 0;


    /**
     * Compiles a collection of directories containing test samples
     * Each sample consists of an "in" and an "out" directory
     * The "in" directory contains the file tree supplied as input to the asset pipeline
     * The "out" directory contains the file tree used to validate the asset pipelines output
     * @return A Collection of references to all directories that contain a test-sample
     */
    @Parameterized.Parameters
    public static Collection<File> data() {
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            OSType = 1;
        }

        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            OSType = 1;
        }

        if (!workingDir.exists() && !workingDir.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", workingDir));
        Assert.assertTrue("The tests resource directory (" + res.getAbsolutePath() + ") doesn't exist.",
                res.isDirectory());
        Collection<File> samples = new ArrayList<>(Arrays.asList(Objects.requireNonNull(res.listFiles(File::isDirectory))));

        samples.removeIf(file -> {
            File osFile = new File(file, ".os");
            if (!osFile.exists()) return false;
            try (BufferedReader br = new BufferedReader(new FileReader(osFile))) {
                if(OSType == Integer.parseInt(br.readLine())) return false;

            } catch (IOException e) {
                return true;
            }
            return true;
        });
        return samples;
    }

    //Root directory of the test sample
    private final File testSet;

    //The inputs of the test
    private final File testSetInput;

    //The expected outputs
    private final File testSetOutput;

    public PipelineTest(File testSet) {

        //Validate the structure of the sample
        //Save references to in- and outputs
        this.testSet = testSet;
        Assert.assertTrue(testSet.isDirectory());

        //Since git doesn't like empty directories, create the in and out if either doesn't exist
        testSetInput = new File(testSet, inputDirName);
        if (!testSetInput.exists() && !testSetInput.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", testSetInput));
        Assert.assertTrue("The datasets at " + testSetInput.getAbsolutePath() + " inputs are missing",
                testSetInput.isDirectory());

        testSetOutput = new File(testSet, outputDirName);
        if (!testSetOutput.exists() && !testSetOutput.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", testSetOutput));
        Assert.assertTrue("The datasets at " + testSetOutput.getAbsolutePath() + " outputs are missing",
                testSetOutput.isDirectory());
    }

    /**
     * Set up the test in a temporary working directory
     */
    @Before
    public void setup() throws IOException {
        //clear the working dir
        cleanUp();

        //copy inputs
        Assert.assertTrue("Unable to create the input directory " + inputDir.getAbsolutePath(),
                inputDir.mkdirs());
        FileUtils.copyContentRec(testSetInput, inputDir,  StandardCopyOption.REPLACE_EXISTING);

        //create in and out directories with some junk files
        Assert.assertTrue("Unable to create the tmp directory " + tmpDir.getAbsolutePath(),
                tmpDir.mkdirs());
        placeJunk(tmpDir);

        Assert.assertTrue("Unable to create the output directory " + outputDir.getAbsolutePath(),
                outputDir.mkdirs());
        placeJunk(outputDir);
    }

    //Used to simulate additional files in tmp and out that need to be taken care of by the pipeline
    private static void placeJunk(File dir) throws IOException {

        if (!dir.exists() && !dir.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", dir));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(dir, "test.txt")));
        writer.write("lorem ipsum");
        writer.close();
        File randomDir = new File(dir, "dir");
        if (!randomDir.exists() && !randomDir.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", randomDir));
    }

    /**
     * Run the pipeline over the sample inputs and compare its output to the sample outputs
     */
    @Test
    public void test() {
        Pipeline.main(new String[]{
                inputDir.getAbsolutePath(),
                tmpDir.getAbsolutePath(),
                outputDir.getAbsolutePath()
        });
        boolean comparison;
        try {
            comparison = FileUtils.compareContentRec(outputDir, testSetOutput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue("Wrong Pipeline-output for Test set " + testSet.getAbsolutePath(), comparison);
    }

    /**
     * Clears the working dir
     */
    @AfterClass
    public static void cleanUp(){
        Assert.assertTrue("Unable to clear the working directory " + workingDir.getAbsolutePath(),
                FileUtils.delDirRec(workingDir));
        Assert.assertTrue("Unable to create the working directory " + workingDir.getAbsolutePath(),
                workingDir.mkdirs());
    }

}

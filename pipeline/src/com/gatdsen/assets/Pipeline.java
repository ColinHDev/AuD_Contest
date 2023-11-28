package com.gatdsen.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.gatdsen.util.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Pipeline {

    static final String GROUP_DESCRIPTOR_NAME = ".group";
    static final String EXCLUSIONS_NAME = ".exclude";

    static final String COMPRESSED_BASE_SKIN_CONFIG_NAME = ".cbase";
    static final String UNCOMPRESSED_BASE_SKIN_CONFIG_NAME = ".ubase";

    static final FileFilter FILTER_NORMAL_FILES = File::isFile;

    static final FileFilter FILTER_DIRECTORIES = File::isDirectory;

    static final String TEXTURE_PACKER_DIR = "texture";

    //By default, clear the tmp directory; can be disabled for debugging
    static final boolean CLEAR_TMP_ON_EXIT = true;

    //Setting up Paths
    //Origin of unprocessed assets
    static File fileRoot;
    //Working directory
    static File tmp;
    //Destination for processed assets
    static File output;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        fileRoot = new File(args[0]);
        tmp = new File(args[1]);
        output = new File(args[2]);

        //Clear the tmp directory
        FileUtils.delDirRec(tmp);
        //Attempt to create the tmp directory for resources  in assets/build
        if (!tmp.exists() && !tmp.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", tmp));

        if (CLEAR_TMP_ON_EXIT) tmp.deleteOnExit();


        //Clear the out directory
        FileUtils.delDirRec(output);
        //Attempt to create the out directory
        if (!output.exists() && !output.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", output));

        //Read Excludes
        List<String> exclusionList = getExcludes(fileRoot);

        //Run TexturePacker after preprocessing unless excluded
        if (!exclusionList.contains(TEXTURE_PACKER_DIR)) {


            File texturePackerSrc = new File(fileRoot, TEXTURE_PACKER_DIR);
            if (texturePackerSrc.isDirectory()) {
                texturePacker(texturePackerSrc);
                exclusionList.add(TEXTURE_PACKER_DIR);
            }
        }

        //Copy remaining non-excluded files without processing
        copy(fileRoot, output, exclusionList);
    }

    /**
     * Loads the contents of the directories .exclude file, if available.
     *
     * @param dir Directory to read exclusions for.
     * @return List of all file and directory names that are supposed to be excluded from processing.
     */
    public static List<String> getExcludes(File dir) {
        //Change the group if the directory contains a descriptor
        File exclusions = new File(dir, EXCLUSIONS_NAME);
        List<String> exclusionList = new ArrayList<>();
        exclusionList.add(GROUP_DESCRIPTOR_NAME);
        exclusionList.add(EXCLUSIONS_NAME);
        exclusionList.add(COMPRESSED_BASE_SKIN_CONFIG_NAME);
        exclusionList.add(UNCOMPRESSED_BASE_SKIN_CONFIG_NAME);
        if (exclusions.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(exclusions))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    exclusionList.add(line);
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read list of exclusions at: " + exclusions.getAbsolutePath() +
                        "\nStacktrace: " + e.getMessage());
            }
        }
        return exclusionList;
    }


    /**
     * Organizes textures into groups, determined by .group files, before compiling them into a TextureAtlas.
     * Every group corresponds to a separate set of pages. All pages of a set have to be loaded together during runtime.
     * The default group is 'misc'. The group for all files and subdirectories in a directory is determined the string in a .group file.
     * The group is inherited until overwritten by another .group file
     *
     * @param input The directory to process
     */
    private static void texturePacker(File input) {
        File packerRoot = new File(tmp, TEXTURE_PACKER_DIR);
        //Attempt to create the working directory for texturePacker  in assets/build/tmp/res
        if (!packerRoot.exists() && !packerRoot.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", packerRoot));
        if (CLEAR_TMP_ON_EXIT) packerRoot.deleteOnExit();

        //Move every Texture to a directory corresponding to its group
        moveTextureContents("misc", input, packerRoot);

        File texturePackerOut = new File(output, "texture_atlas");
        if (!texturePackerOut.exists() && !texturePackerOut.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", texturePackerOut));

        TexturePacker.Settings settings = new TexturePacker.Settings();

        //Einstellungen, um die Skalierung auf NearestNeighbour zu setzen
        settings.filterMag = Texture.TextureFilter.Nearest;
        settings.filterMin = Texture.TextureFilter.Nearest;

        TexturePacker.process(settings, packerRoot.getAbsolutePath(), texturePackerOut.getAbsolutePath(), "TextureAtlas");
    }

    /**
     * Recursively moves Files to their correct group in tmp/res/text/texture directory to prepare for texture packing.
     * <p>
     * Groups are defined by a .group file in the directory: they contain the name of the group that should be used for
     * the files of this directory and all subdirectories unless overwritten.
     * If no group is given the group of the parent directory is assigned.
     * thus groups in subdirectories overwrite groups from higher directories.
     * <p>
     * .exclude files list the names of all files and subdirectories that should be excluded from processing
     * <p>
     * After moving all required files to their corresponding group, the subdirectories are traversed recursively.
     *
     * @param group               Parent-group, will be used if no .group file in the directory overwrites it
     * @param dir                 directory to be traversed
     * @param texturePackerOutput working-directory of the TexturePacker
     */
    private static void moveTextureContents(String group, File dir, File texturePackerOutput) {
        if (dir == null) throw new RuntimeException("Subdirectory may not be null");
        if (!dir.exists()) throw new RuntimeException("Subdirectory at " + dir.getAbsolutePath() + " does not exist");

        List<String> exclusionList = getExcludes(dir);

        //Change the group if the directory contains a descriptor
        File groupDescriptor = new File(dir, GROUP_DESCRIPTOR_NAME);
        if (groupDescriptor.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(groupDescriptor))) {
                group = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read group descriptor at: " + groupDescriptor.getAbsolutePath() +
                        "\nStacktrace: " + e.getMessage());
            }
        }

        File compressedBaseSkinDescriptor = new File(dir, COMPRESSED_BASE_SKIN_CONFIG_NAME);
        File uncompressedBaseSkinDescriptor = new File(dir, UNCOMPRESSED_BASE_SKIN_CONFIG_NAME);
        Map<RGBColor, int[]> blueprintEncoding = null;
        int[][][] skinEncoding = null;
        int[] compressedSkinSize = new int[2];
        if (compressedBaseSkinDescriptor.exists()) {
            BufferedImage compressedBaseSkin;
            // Load Compressed Template
            try (BufferedReader reader = new BufferedReader(new FileReader(compressedBaseSkinDescriptor))) {
                File compressedBaseSkinPath = new File(fileRoot, reader.readLine());
                System.out.println(compressedBaseSkinPath.getCanonicalPath());
                if (!isImage(compressedBaseSkinPath))
                    throw new RuntimeException("The supplied reverse lookup file has an invalid format.");
                try {
                    compressedBaseSkin = ImageIO.read(compressedBaseSkinPath);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read the supplied reverse lookup file.");
                }
            } catch (IOException e) {
                throw new RuntimeException("Couldn't read lookup descriptor at: " + compressedBaseSkinDescriptor.getAbsolutePath() +
                        "\nStacktrace: " + e.getMessage());
            }
            if (uncompressedBaseSkinDescriptor.exists()) {
                //Load skin template
                try (BufferedReader reader = new BufferedReader(new FileReader(uncompressedBaseSkinDescriptor))) {
                    File uncompressedBaseSkinPath = new File(fileRoot, reader.readLine());
                    if (!isImage(uncompressedBaseSkinPath))
                        throw new RuntimeException("The supplied reverse lookup file has an invalid format.");
                    try {
                        skinEncoding = generateSkinEncoding(compressedBaseSkin, ImageIO.read(uncompressedBaseSkinPath), compressedSkinSize);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read the supplied reverse lookup file.");
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't read lookup descriptor at: " + compressedBaseSkinDescriptor.getAbsolutePath() +
                            "\nStacktrace: " + e.getMessage());
                }


            } else {
                blueprintEncoding = generateBlueprintEncoding(compressedBaseSkin);
            }
        }


        //Copy all files to the correct group
        File[] content = dir.listFiles(FILTER_NORMAL_FILES);
        File groupPath = new File(texturePackerOutput, group);
        //Create the group directory if it doesn't exist already
        if (!groupPath.exists() && !groupPath.mkdirs())
            throw new RuntimeException(String.format("Could not create %s directory", groupPath));
        if (CLEAR_TMP_ON_EXIT) groupPath.deleteOnExit();
        if (content == null) return;
        Arrays.sort(content);
        for (File cur :
                content) {

            if (!exclusionList.contains(cur.getName()))
                try {
                    File dest = new File(groupPath, cur.getName());
//                    System.out.println(cur.getName() + !isImage(cur) + "," + (blueprintEncoding == null) + "," + (skinEncoding == null));
                    if (isImage(cur))
                        if (skinEncoding != null)
                            skinPreprocess(cur, skinEncoding, compressedSkinSize, dest);
                        else if (blueprintEncoding != null)
                            blueprintPreProcess(cur, blueprintEncoding, dest);
                        else
                            Files.copy(cur.toPath(), dest.toPath());

                    if (CLEAR_TMP_ON_EXIT) dest.deleteOnExit();
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't copy file at: " + cur.getAbsolutePath() +
                            "\nStacktrace: " + e.getMessage());
                }
        }

        //Recursively traverse every subdirectory
        File[] subDirs = dir.listFiles(FILTER_DIRECTORIES);
        if (subDirs == null) return;
        Arrays.sort(subDirs);
        for (File cur :
                subDirs) {
            if (!exclusionList.contains(cur.getName()))
                moveTextureContents(group, cur, texturePackerOutput);
        }
    }


    /**
     * Generates an encoding used for converting raw blueprints to encoded blueprints, where all reference colors have been replaced by their positional encoding.
     *
     * @param compressedBaseSkin     a compressed skin containing reference colors
     * @return      Maps reference colors to positions on a compressed skin
     */
    private static Map<RGBColor, int[]> generateBlueprintEncoding(BufferedImage compressedBaseSkin) {
        Map<RGBColor, int[]> encoding = new HashMap<>();
        int width = compressedBaseSkin.getWidth();
        int height = compressedBaseSkin.getHeight();
        if (!(isValidEncodingSize(width) | isValidEncodingSize(height))) {
            throw new RuntimeException("The compressed base skin has to be smaller than or equal to 256 x 256 and each side needs to be a power of 2");
        }

        int xScale = 256 / width;
        int yScale = 256 / height;

        for (int x = 0; x < compressedBaseSkin.getWidth(); x++)
            for (int y = 0; y < compressedBaseSkin.getHeight(); y++) {
                int[] colorA = compressedBaseSkin.getRaster().getPixel(x, y, (int[]) null);
                if (colorA[3] == 0) continue;
                RGBColor color = new RGBColor(new int[]{colorA[0], colorA[1], colorA[2]});
                if (encoding.get(color) == null) {
                    encoding.put(color, new int[]{x * xScale, y * yScale});
                } else {
                    if (!Arrays.equals(encoding.get(color), new int[]{256 - (x + 1) * xScale, y * yScale}))
                        throw new RuntimeException("A duplicate color may only appear at the mirrored x coordinate");
                }

            }
        return encoding;
    }

    private static boolean isValidEncodingSize(int size) {
        return size < 257 && ((size & size - 1) == 0);
    }

    /**
     * Generates the encoding used for compressing skins
     *
     * @param compressedBaseSkin   A compressed Skin colored with the reference colors
     * @param uncompressedBaseSkin An uncompressed Skin colored with the reference colors
     * @param out_compressedSkinSize an int[2] Array where the size of the compressed skin will be written to
     * @return Maps positions on the compressed skin to positions on the uncompressed skin, where colors should be retrieved from
     */
    private static int[][][] generateSkinEncoding(BufferedImage compressedBaseSkin, BufferedImage uncompressedBaseSkin, int[] out_compressedSkinSize) {

        int width = compressedBaseSkin.getWidth();
        int width2 = uncompressedBaseSkin.getWidth();
        out_compressedSkinSize[0] = width;
        int height = compressedBaseSkin.getHeight();
        int height2 = uncompressedBaseSkin.getHeight();
        out_compressedSkinSize[1] = height;
        int[][][] skinEncoding = new int[width][height][2];
        int[] defaultPos = new int[]{-1, -1};
        WritableRaster compressedRaster = compressedBaseSkin.getRaster();
        WritableRaster uncompressedRaster = uncompressedBaseSkin.getRaster();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                int[] colorA = compressedRaster.getPixel(x, y, (int[]) null);
                if (Arrays.equals(colorA, new int[4])) continue;
                int[] pos = defaultPos;
                for (int x2 = 0; x2 < width2; x2++)
                    for (int y2 = 0; y2 < height2; y2++) {
                        if (Arrays.equals(colorA, uncompressedRaster.getPixel(x2, y2, (int[]) null))) {
                            pos = new int[]{x2, y2};
                        }
                    }
                skinEncoding[x][y] = pos;
            }
        return skinEncoding;
    }

    /**
     * Preprocesses a single File that utilizes Texture Lookup
     * Replaces all Pixels in the raw blueprint with their positional encoding, based on where that color is found in the compressed base-skin.
     * After Processing, applying the supplied compressed base-skin (specified in .lookup) on the result should give the src.
     *
     * @param src               Image to be transformed
     * @param blueprintEncoding Maps colors to positions
     * @param dest              Output Path
     */
    private static void blueprintPreProcess(File src, Map<RGBColor, int[]> blueprintEncoding, File dest) {
        try {
            BufferedImage srcImage = ImageIO.read(src);
            BufferedImage lightMap;
            WritableRaster lightRaster = null;
            File lightMapFile = new File(src.getParentFile(), src.getName() + ".light");
            if (lightMapFile.exists()) {
                lightMap = ImageIO.read(lightMapFile);
                lightRaster = lightMap.getRaster();
            }
            String srcName = src.getName();
            int[] defaultPos = new int[]{0, 0};
            BufferedImage destImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            WritableRaster srcRaster = srcImage.getRaster();
            WritableRaster destRaster = destImage.getRaster();
            for (int x = 0; x < srcImage.getWidth(); x++)
                for (int y = 0; y < srcImage.getHeight(); y++) {
                    int[] colorA = srcRaster.getPixel(x, y, (int[]) null);
                    RGBColor color = new RGBColor(new int[]{colorA[0], colorA[1], colorA[2]});
                    int[] posColor = blueprintEncoding.getOrDefault(color, defaultPos);

                    int lightLevel = 127;
                    if (lightRaster != null) {
                        int[] light = lightRaster.getPixel(x, y, (int[]) null);
                        lightLevel = (light[0] + light[1] + light[2])/3;
                    }

                        destRaster.setPixel(x, y, new int[]{posColor[0], posColor[1], lightLevel, colorA[3]});
                }

            ImageIO.write(destImage, srcName.substring(srcName.lastIndexOf('.') + 1), dest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compresses a skin into the format used for lookup at runtime according to the differences between uncompressed base-skin and compressed base-skin.
     *
     * @param src          Uncompressed skin to be transformed
     * @param skinEncoding Maps positions on the uncompressed skin to positions on the compressed skin
     * @param size         Size of the compressed Skin
     * @param dest         Output Path
     */
    private static void skinPreprocess(File src, int[][][] skinEncoding, int[] size, File dest) {
        try {
            BufferedImage srcImage = ImageIO.read(src);
            BufferedImage resultImage = new BufferedImage(size[0], size[1], srcImage.getType());
            String srcName = src.getName();
            WritableRaster srcRaster = srcImage.getRaster();
            WritableRaster resultRaster = resultImage.getRaster();
            for (int x = 0; x < size[0]; x++)
                for (int y = 0; y < size[1]; y++) {
                    int[] pos = skinEncoding[x][y];
                    if (!Arrays.equals(pos, new int[]{-1, -1}))
                        resultRaster.setPixel(x, y, srcRaster.getPixel(pos[0], pos[1], (int[]) null));
                }
            ImageIO.write(resultImage, srcName.substring(srcName.lastIndexOf('.') + 1), dest);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static boolean isImage(File path) {
        return path.isFile() && (path.getName().endsWith(".png") || path.getName().endsWith(".jpg"));
    }

    /**
     * Recursively copies all files and subdirectories from one directory to another.
     * Ignores files and subDirs listed in .exclude files.
     * The excludes parameter is passed to give the caller an opportunity for selectively copying certain directories.
     *
     * @param srcDir   directory to copy files from
     * @param destDir  directory to copy files to
     * @param excludes list of files and directories to be excluded
     */
    private static void copy(File srcDir, File destDir, List<String> excludes) {

        File[] content = srcDir.listFiles(FILTER_NORMAL_FILES);
        if (content == null) return;
        for (File cur :
                content) {

            if (!excludes.contains(cur.getName()))
                try {
                    File dest = new File(destDir, cur.getName());
                    Files.copy(cur.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Couldn't copy file at: " + cur.getAbsolutePath() +
                            "\nStacktrace: " + e.getMessage());
                }
        }

        //Recursively traverse every subdirectory
        File[] subDirs = srcDir.listFiles(FILTER_DIRECTORIES);
        if (subDirs == null) return;
        for (File cur :
                subDirs) {
            if (!excludes.contains(cur.getName())) {
                File destCur = new File(destDir, cur.getName());
                if (!destCur.exists() && !destCur.mkdirs())
                    throw new RuntimeException(String.format("Could not create %s directory", destCur));
                List<String> newExcludes = getExcludes(cur);
                copy(cur, new File(destDir, cur.getName()), newExcludes);
            }
        }
    }


}

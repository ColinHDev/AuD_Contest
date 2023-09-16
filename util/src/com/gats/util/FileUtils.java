package com.gats.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Some advanced tools for manipulating entire File trees
 */
public class FileUtils {

    /**
     * Compares the contents and structure of the two given directories for bitwise equality
     * Only the paths relative to the given roots (dir1, dir2) has to be identical
     * dir1 and dir2 may be named differently. Only the contents are compared
     * @param dir1 First directory
     * @param dir2 Second directory
     * @return Whether the contents of both directories are identical
     * @throws IOException In case one or more files can't be read
     */
    public static boolean compareContentRec(File dir1, File dir2) throws IOException {
        File[] list1 = dir1.listFiles();
        File[] list2 = dir2.listFiles();
        if (list1 == null) throw new RuntimeException("Error reading files from " + dir1.getAbsolutePath());
        if (list2 == null) throw new RuntimeException("Error reading files from " + dir2.getAbsolutePath());

        if (list1.length != list2.length) return false;

        for (File cur : list1) {
            File cur2 = new File(dir2, cur.getName());
            if (cur.isDirectory()) {
                if (!cur.exists() || !cur2.exists()) return false;
                if (!compareContentRec(cur, cur2)) return false;
            } else {
                if (!compareFiles(cur, cur2)) return false;
            }
        }

        return true;
    }

    /**
     * Compares two files for bitwise equality
     * Their names need to be identical, but not their paths
     * @param f1 First file
     * @param f2 Second file
     * @return Whether both files are identical
     * @throws IOException When a file cannot be read
     */
    public static boolean compareFiles(File f1, File f2) throws IOException {
        if (!f1.getName().equals(f2.getName())) return false;
        if (!f1.exists() || !f2.exists()) return false;

        //Get file input stream for reading the file content
        FileInputStream fis1 = new FileInputStream(f1);
        FileInputStream fis2 = new FileInputStream(f2);

        //Create byte array to read data in chunks
        byte[] byteArray1 = new byte[1024];
        byte[] byteArray2 = new byte[1024];
        int bytesCount = 0;

        //Read file data and update in message digest
        while ((bytesCount = fis1.read(byteArray1)) != -1) {
            if (bytesCount != fis2.read(byteArray2)) {
                //file1 is longer than file 2
                fis1.close();
                fis2.close();
                return false;
            }

            for (int i = 0; i < bytesCount; i++)
                if (byteArray1[i] != byteArray2[i]) {
                    //content mismatch
                    fis1.close();
                    fis2.close();
                    return false;
                }
        }
        if (fis2.read(byteArray2) != -1) {
            //file2 is longer than file 1
            fis1.close();
            fis2.close();
            return false;
        }

        fis1.close();
        fis2.close();

        return true;
    }


    /**
     * Recursively copies the contents, including all subdirectories and their contents, of one directory to another
     * @param srcDir Directory to copy from
     * @param dest Directory to copy to
     * @throws IOException When the duplication of one or more Files failed
     */
    public static void copyContentRec(File srcDir, File dest, CopyOption copyOption) throws IOException {
        File[] contents = srcDir.listFiles();
        if (contents != null) {
            for (File cur : contents) {
                if (cur.isDirectory()) {
                    File subdir = new File(dest, cur.getName());
                    subdir.mkdir();
                    copyContentRec(cur, subdir, copyOption);
                } else {
                        Files.copy(cur.toPath(), new File(dest, cur.getName()).toPath(), copyOption);
                }
            }
        }
    }

    /**
     * Recursively deletes a directory including all files and subdirectories
     * @param dir Directory to be deleted
     * @return true if, and only if dir and all contents have been successfully deleted
     */
    public static boolean delDirRec(File dir) {
        File[] contents = dir.listFiles();
        if (contents != null) {
            for (File cur : contents) {
                delDirRec(cur);
            }
        }
        return dir.delete();
    }
}

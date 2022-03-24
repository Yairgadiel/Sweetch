package com.gy.sweetch.utils;

import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;

public final class FileUtils {

    /**
     * This method creates a file in a given path from the given response
     * @param body the response containing the image's bytes
     * @param filePath the path of the file to create
     * @return a boolean indicating whether the operation was successful
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String filePath) {
        boolean isSuccess = false;

        try {
            File imgFile = new File(filePath);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                inputStream = body.byteStream();

                // Decode the zip file
                outputStream = new FileOutputStream(imgFile);
                outputStream.write(body.bytes());

                isSuccess = true;
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    /**
     * This method clears the images directory from all previous files
     * @param dir the dir to clear
     */
    public static void clearDir(String dir) {
        File filesDir = new File(dir);

        File[] files = filesDir.listFiles();

        // If there are no files in the folder it means that this is the first run
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * This method unzips a file in the given path
     * @param path the path of the file to unzip
     * @param zipname the name of the file to unzip
     * @return boolean indicating whether the operation was successful
     */
    public static boolean unzip(String path, String zipname) {
        InputStream is;
        ZipInputStream zis;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                filename = ze.getName();

                FileOutputStream fout = new FileOutputStream(path + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * This method finds an image file (png) in a given dir
     * @param dirPath the path to search the image in
     * @return the first found image file, null if not found
     */
    @Nullable
    public static File findImage(String dirPath) {
        File image = null;


        File filesDir = new File(dirPath);

        // Checking if file directory is valid
        if (filesDir.exists() && filesDir.isDirectory()) {
            File[] files = filesDir.listFiles();

            if (files != null) {

                // Searching for an image
                for (File file : files) {
                    if (file.getName().endsWith(".png")) {
                        image = file;

                        break;
                    }
                }
            }
        }

        return image;
    }
}

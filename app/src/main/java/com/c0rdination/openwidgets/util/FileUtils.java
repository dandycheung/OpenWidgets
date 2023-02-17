package com.c0rdination.openwidgets.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void createDirIfNotExists(String path) {
        File file = new File(fixPathSeparator(path));
        if (!file.exists())
            file.mkdirs();
    }

    private static void createNew(String path) {
        int lastSep = fixPathSeparator(path).lastIndexOf(File.separator);
        if (lastSep > 0) {
            String dirPath = fixPathSeparator(path).substring(0, lastSep);
            createDirIfNotExists(dirPath);
            File folder = new File(dirPath);
            folder.mkdirs();
        }

        File file = new File(fixPathSeparator(path));

        try {
            if (!file.exists())
                file.createNewFile();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
    }

    public static String read(String path, String defaultValue) {
        String content = read(path);
        return content != null && content.equals("") ? defaultValue : content;
    }

    public static String read(String path) {
        try {
            createNew(fixPathSeparator(path));
            StringBuilder stringBuilder = new StringBuilder();
            FileReader fileReader = null;

            try {
                fileReader = new FileReader(fixPathSeparator(path));
                char[] buff = new char[1024];

                int length;
                while ((length = fileReader.read(buff)) > 0)
                    stringBuilder.append(new String(buff, 0, length));
            } catch (IOException var14) {
                var14.printStackTrace();
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception var13) {
                        var13.printStackTrace();
                    }
                }
            }

            return stringBuilder.toString();
        } catch (Exception var16) {
            return null;
        }
    }

    public static boolean write(String path, String content) {
        try {
            createNew(fixPathSeparator(path));
            FileWriter fileWriter = new FileWriter(fixPathSeparator(path), false);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean isExist(String path) {
        return (new File(path.replace("/", File.separator))).isFile();
    }

    public static String fixPathSeparator(String path) {
        return path.replace("/", File.separator).replace("\\", File.separator);
    }
}

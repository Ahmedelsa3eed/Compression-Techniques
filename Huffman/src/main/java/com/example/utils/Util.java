package com.example.utils;

import java.nio.file.Path;

public class Util {

    public static String formulateCompressedFilePath(String filePath, int n) {
        Path path = Path.of(filePath);
        String fileName = path.getFileName().toString();
        String parentName = path.getParent().toString();
        return parentName + "/" + n + '.' + fileName + ".hc";
    }

    public static String formulateDecompressedFilePath(String filePath) {
        Path path = Path.of(filePath);
        String fileName = path.getFileName().toString();
        String parentName = path.getParent().toString();
        return parentName + "/extracted." + fileName.substring(0, fileName.lastIndexOf('.'));
    }

}

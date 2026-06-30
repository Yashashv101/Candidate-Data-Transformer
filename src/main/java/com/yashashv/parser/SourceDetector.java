package com.yashashv.parser;

public class SourceDetector {
    public static String detect(String filePath) {
        String file = filePath.toLowerCase();
        if (file.endsWith(".csv")) return "CSV";
        if (file.endsWith(".json")) return "JSON";
        if (file.endsWith(".txt")) return "TEXT";
        return "UNKNOWN";
    }
}

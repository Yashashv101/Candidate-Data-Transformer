package com.yashashv.parser;

import com.yashashv.model.SourceType;

public class SourceDetector {
    public static SourceType detect(String filePath) {
        String file = filePath.toLowerCase();
        if (file.endsWith(".csv")) return SourceType.CSV;
        if (file.endsWith(".json")) return SourceType.JSON;
        if (file.endsWith(".txt")) return SourceType.TXT;
        throw new IllegalArgumentException("Unsupported File Type");
    }
}

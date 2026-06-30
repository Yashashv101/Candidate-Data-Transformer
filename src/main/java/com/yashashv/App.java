package com.yashashv;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.parser.*;

import java.io.IOException;
import java.util.List;
public class App 
{
    public static void main( String[] args ) throws IOException {
        String filePath = "";
        String type = SourceDetector.detect(filePath);
        Parser parser;
        switch (type) {
            case "CSV":
                parser = new CsvParser();
                break;
            case "JSON":
                parser = new JsonParser();
                break;
            case "TEXT":
                parser = new TextParser();
                break;
            default:
                throw new IllegalArgumentException("Unsupported File");
        }
        List<CanonicalRecord> records = parser.parse(filePath);

    }
}

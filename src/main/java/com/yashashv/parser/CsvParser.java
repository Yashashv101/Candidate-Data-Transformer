package com.yashashv.parser;

import com.yashashv.model.CanonicalRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser implements Parser{
    @Override
    public List<CanonicalRecord> parse(String filePath) throws IOException {
        List<CanonicalRecord> records = new ArrayList<>();
        FileReader reader = new FileReader(filePath);
        CSVParser parser = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build()
                .parse(reader);
        for (CSVRecord row : parser) {
            CanonicalRecord record = new CanonicalRecord();
            record.setFullName(row.get("full_name"));
            record.getEmails().add(row.get("email"));
            record.getPhones().add(row.get("phone"));
            record.setHeadline(row.get("headline"));
            record.setLocation(row.get("location"));
            records.add(record);
        }
        parser.close();
        reader.close();
        return records;
    }

}

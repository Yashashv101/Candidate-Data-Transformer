package com.yashashv.parser;

import com.yashashv.model.CanonicalRecord;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TextParser implements Parser {
    @Override
    public List<CanonicalRecord> parse(String filePath) throws IOException {
        CanonicalRecord record = new CanonicalRecord();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Name")) record.setFullName(line.substring(4).trim());
            else if (line.startsWith("Email")) record.getEmails().add(line.substring(5).trim());
            else if (line.startsWith("Phone")) record.getPhones().add(line.substring(5).trim());
            else if (line.startsWith("Headline")) record.setHeadline(line.substring(8).trim());
            else if (line.startsWith("Location")) record.setLocation(line.substring(8).trim());
        }
        reader.close();
        return List.of(record);
    }
}

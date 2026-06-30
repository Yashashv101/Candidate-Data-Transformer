package com.yashashv.parser;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.SourceType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TextParser implements Parser {
    @Override
    public List<CanonicalRecord> parse(String filePath) throws IOException {
        CanonicalRecord record = new CanonicalRecord();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String value = extractValue(line);
            if (line.startsWith("Name:"))      record.setFullName(value);
            else if (line.startsWith("Email:"))    record.getEmails().add(value);
            else if (line.startsWith("Phone:"))    record.getPhones().add(value);
            else if (line.startsWith("Headline:")) record.setHeadline(value);
            else if (line.startsWith("Location:")) record.setLocation(value);
            else if (line.startsWith("Skills:"))   record.getSkills().addAll(
                Arrays.stream(value.split(",")).map(String::trim).toList()
            );
        }
        record.setSourceType(SourceType.TXT);
        reader.close();
        return List.of(record);
    }

    private String extractValue(String line) {
        int colon = line.indexOf(':');
        if (colon == -1) return line.trim();
        return line.substring(colon + 1).trim();
    }
}

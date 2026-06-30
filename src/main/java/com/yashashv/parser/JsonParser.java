package com.yashashv.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashashv.model.CanonicalRecord;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser implements Parser{
    @Override
    public List<CanonicalRecord> parse(String filePath) throws IOException {
        List<CanonicalRecord> records = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(filePath));
        for (JsonNode node : root) {
            CanonicalRecord record = new CanonicalRecord();
            record.setFullName(node.path("full_name").asText());
            record.getEmails().add(node.path("email").asText());
            record.getPhones().add(node.path("phone").asText());
            record.setHeadline(node.path("headline").asText());
            record.setLocation(node.path("location").asText());
            records.add(record);
        }
        return records;
    }
}

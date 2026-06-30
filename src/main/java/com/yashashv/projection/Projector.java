package com.yashashv.projection;

import com.yashashv.model.CanonicalRecord;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Projector {
    public List<Map<String, Object>> project(List<CanonicalRecord> records,Config config){
        List<Map<String, Object>> output = new java.util.ArrayList<>();
        for (CanonicalRecord record : records) {
            output.add(projectRecord(record, config));
        }
        return output;
    }
    private Map<String, Object> projectRecord(CanonicalRecord record, Config config) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (String field : config.getFields()) {
            Object value = getFieldValue(record, field);
            if (value == null) {
                if ("omit".equalsIgnoreCase(config.getOnMissing())) continue;
                if ("error".equalsIgnoreCase(config.getOnMissing()))
                    throw new IllegalArgumentException("Missing required field : " + field);
            }
            String outputField = config.getRenameFields().getOrDefault(field, field);
            result.put(outputField, value);
        }
        if (config.isIncludeConfidence()) result.put("confidence", record.getConfidence());
        if (config.isIncludeProvenance()) result.put("provenance", record.getProvenance());
        return result;
    }
    private Object getFieldValue(CanonicalRecord record, String field) {
        return switch (field) {
            case "candidateId" -> record.getCandidateID();
            case "fullName" -> record.getFullName();
            case "headline" -> record.getHeadline();
            case "location" -> record.getLocation();
            case "emails" -> record.getEmails();
            case "phones" -> record.getPhones();
            case "skills" -> record.getSkills();
            case "experience" -> record.getExperiences();
            case "education" -> record.getEducation();
            default -> null;
        };
    }
}

package com.yashashv.validator;

import com.yashashv.projection.Config;
import java.util.List;
import java.util.Map;

public class Validator {
    public void validate(List<Map<String, Object>> records, Config config) {
        for (Map<String, Object> record : records) {
            validateRecord(record, config);
        }
    }
    private void validateRecord(Map<String, Object> record, Config config) {
        for (String field : config.getFields()) {
            if ("omit".equalsIgnoreCase(config.getOnMissing())) continue;
            if (!record.containsKey(field)) {
                throw new IllegalArgumentException(
                        "Missing required field: " + field
                );
            }
            Object value = record.get(field);
            if ("error".equalsIgnoreCase(config.getOnMissing()) && value == null)
                throw new IllegalArgumentException("Null value found for required field: " + field);
            validateFieldType(field, value);
        }
    }
    private void validateFieldType(String field, Object value) {
        if (value == null) return;
        switch (field) {
            case "candidateId":
            case "fullName":
            case "headline":
            case "location":
                if (!(value instanceof String)) throw new IllegalArgumentException(field + " must be a String");
                break;
            case "emails":
            case "phones":
            case "skills":
            case "experience":
            case "education":
                if (!(value instanceof List)) throw new IllegalArgumentException(field + " must be a List");
                break;
            case "confidence":
                if (!(value instanceof Double)) throw new IllegalArgumentException("confidence must be a Double");
                break;
            default:
                break;
        }
    }
}
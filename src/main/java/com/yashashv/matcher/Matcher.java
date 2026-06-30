package com.yashashv.matcher;

import com.yashashv.model.CanonicalRecord;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Matcher {
    public List<List<CanonicalRecord>> match(List<CanonicalRecord> records) {
        Map<String,List<CanonicalRecord>> groupedRecords=new LinkedHashMap<>();
        List<CanonicalRecord> unmatchedRecords=new ArrayList<>();
        for (CanonicalRecord record:records) {
            String matchKey=generateMatchKey(record);
            if (matchKey==null){
                unmatchedRecords.add(record);
                continue;
            }
            groupedRecords.computeIfAbsent(matchKey,key->new ArrayList<>()).add(record);
        }
        for(CanonicalRecord record:unmatchedRecords) {
            List<CanonicalRecord> group=new ArrayList<>();
            group.add(record);
            groupedRecords.put("UNMATCHED_"+System.identityHashCode(record),group);
        }
        return new ArrayList<>(groupedRecords.values());
    }
    private String generateMatchKey(CanonicalRecord record){
        if(record.getEmails()!=null && !record.getEmails().isEmpty()) {
            String email=record.getEmails().get(0);
            if (email!=null && !email.isBlank()) return "EMAIL:" + email;
        }
        if (record.getFullName() != null && !record.getFullName().isBlank() && record.getPhones() != null && !record.getPhones().isEmpty()) {
            String phone = record.getPhones().get(0);
            if (phone != null && !phone.isBlank()) return "NAME_PHONE:" + record.getFullName().trim().toLowerCase() + "|" + phone;
        }
        return null;
    }
}

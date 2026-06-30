package com.yashashv.normalizer;
import com.yashashv.model.CanonicalRecord;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RecordCleaner {
    public CanonicalRecord clean(CanonicalRecord record) {
        for(int i=0;i<record.getEmails().size();i++){
            record.getEmails().set(i,EmailNormalizer.normalize(record.getEmails().get(i)));
        }
        for(int i=0;i<record.getPhones().size();i++){
            record.getPhones().set(i, PhoneNormalizer.normalize(record.getPhones().get(i)));
        }
        for(int i=0;i<record.getSkills().size();i++){
            record.getSkills().set(i, SkillNormalizer.normalize(record.getSkills().get(i)));
        }
        cleanLists(record);
        return record;
    }
    private void cleanLists(CanonicalRecord record) {
        record.getEmails().removeIf(email -> email == null || email.isBlank());
        record.getPhones().removeIf(phone -> phone == null || phone.isBlank());
        record.getSkills().removeIf(skill -> skill == null || skill.isBlank());
        //removing duplicates
        record.setEmails(new ArrayList<>(new LinkedHashSet<>(record.getEmails())));
        record.setPhones(new ArrayList<>(new LinkedHashSet<>(record.getPhones())));
        record.setSkills(new ArrayList<>(new LinkedHashSet<>(record.getSkills())));
    }
}
package com.yashashv.normalizer;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.Experience;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class RecordCleaner {
    public CanonicalRecord clean(CanonicalRecord record) {
        for(int i=0;i<record.getEmails().size();i++){
            record.getEmails().set(i, EmailNormalizer.normalize(record.getEmails().get(i)));
        }
        for(int i=0;i<record.getPhones().size();i++){
            record.getPhones().set(i, PhoneNormalizer.normalize(record.getPhones().get(i)));
        }
        for(int i=0;i<record.getSkills().size();i++){
            record.getSkills().set(i, SkillNormalizer.normalize(record.getSkills().get(i)));
        }
        normalizeDates(record);
        cleanLists(record);
        return record;
    }
    private void normalizeDates(CanonicalRecord record) {
        List<Experience> normalized=new ArrayList<>();
        for(Experience exp:record.getExperiences()){
            normalized.add(new Experience(
                exp.company(),
                exp.role(),
                DateNormalizer.normalize(exp.startDate()),
                DateNormalizer.normalize(exp.endDate())
            ));
        }
        record.setExperiences(normalized);
    }
    private void cleanLists(CanonicalRecord record){
        record.getEmails().removeIf(e->e==null || e.isBlank());
        record.getPhones().removeIf(p->p==null || p.isBlank());
        record.getSkills().removeIf(s->s==null || s.isBlank());
        record.setEmails(new ArrayList<>(new LinkedHashSet<>(record.getEmails())));
        record.setPhones(new ArrayList<>(new LinkedHashSet<>(record.getPhones())));
        record.setSkills(new ArrayList<>(new LinkedHashSet<>(record.getSkills())));
    }
}

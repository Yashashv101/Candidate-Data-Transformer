package com.yashashv.merger;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.Provenance;
import com.yashashv.model.SourceType;
import com.yashashv.util.Constants;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Merger {
    public List<CanonicalRecord> merge(List<List<CanonicalRecord>> groups) {
        List<CanonicalRecord> mergedRecords = new ArrayList<>();
        for (List<CanonicalRecord> group : groups) {
            mergedRecords.add(mergeGroup(group));
        }
        return mergedRecords;
    }
    private CanonicalRecord mergeGroup(List<CanonicalRecord> group) {
        CanonicalRecord merged = new CanonicalRecord();
        CanonicalRecord trustedRecord = getHighestTrustedRecord(group);
        merged.setCandidateID(generateCandidateId(group));
        merged.setFullName(trustedRecord.getFullName());
        merged.setHeadline(trustedRecord.getHeadline());
        merged.setLocation(trustedRecord.getLocation());
        merged.setEmails(mergeEmails(group));
        merged.setPhones(mergePhones(group));
        merged.setSkills(mergeSkills(group));
        merged.setExperiences(mergeExperiences(group));
        merged.setEducation(mergeEducation(group));
        merged.setProvenance(buildProvenance(group));
        merged.setSourceType(trustedRecord.getSourceType());
        return merged;
    }
    private CanonicalRecord getHighestTrustedRecord(List<CanonicalRecord> group) {
        CanonicalRecord best = group.get(0);
        for (CanonicalRecord record : group) {
            if (getTrust(record.getSourceType()) > getTrust(best.getSourceType())) best = record;
        }
        return best;
    }
    private double getTrust(SourceType sourceType) {
        return switch (sourceType) {
            case CSV -> Constants.CSV_TRUST;
            case JSON -> Constants.JSON_TRUST;
            case TXT -> Constants.TXT_TRUST;
            case PDF -> Constants.PDF_TRUST;
        };
    }
    private List<String> mergeEmails(List<CanonicalRecord> group) {
        LinkedHashSet<String> emails = new LinkedHashSet<>();
        for (CanonicalRecord record : group) {
            emails.addAll(record.getEmails());
        }
        return new ArrayList<>(emails);
    }
    private List<String> mergePhones(List<CanonicalRecord> group) {
        LinkedHashSet<String> phones = new LinkedHashSet<>();
        for (CanonicalRecord record : group) {
            phones.addAll(record.getPhones());
        }
        return new ArrayList<>(phones);
    }
    private List<String> mergeSkills(List<CanonicalRecord> group) {
        LinkedHashSet<String> skills = new LinkedHashSet<>();
        for (CanonicalRecord record : group) {
            skills.addAll(record.getSkills());
        }
        return new ArrayList<>(skills);
    }
    private List mergeExperiences(List<CanonicalRecord> group) {
        LinkedHashSet experiences = new LinkedHashSet();
        for (CanonicalRecord record : group) {
            experiences.addAll(record.getExperiences());
        }
        return new ArrayList<>(experiences);
    }
    private List mergeEducation(List<CanonicalRecord> group) {
        LinkedHashSet education = new LinkedHashSet();
        for (CanonicalRecord record : group) {
            education.addAll(record.getEducation());
        }
        return new ArrayList<>(education);
    }
    private List<Provenance> buildProvenance(List<CanonicalRecord> group) {
        List<Provenance> provenance = new ArrayList<>();
        for (CanonicalRecord record : group) {
            provenance.addAll(record.getProvenance());
        }
        return provenance;
    }
    private String generateCandidateId(List<CanonicalRecord> group) {
        CanonicalRecord record = group.get(0);
        if (!record.getEmails().isEmpty()) return record.getEmails().get(0);
        return "CAND-" + System.nanoTime();
    }
}

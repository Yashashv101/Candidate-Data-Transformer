package com.yashashv.confidence;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.SourceType;
import com.yashashv.util.Constants;
import java.util.List;

public class ConfidenceCalc {
    public void calculate(List<CanonicalRecord> records){
        for (CanonicalRecord record:records) {
            record.setConfidence(calculateConfidence(record));
        }
    }
    private double calculateConfidence(CanonicalRecord record) {
        double trustScore=getTrustScore(record.getSourceType());
        double agreementScore=calculateAgreementScore(record);
        double confidence=trustScore*agreementScore;
        return Math.min(confidence, 1.0);
    }
    private double getTrustScore(SourceType sourceType) {
        return switch (sourceType) {
            case CSV->Constants.CSV_TRUST;
            case JSON->Constants.JSON_TRUST;
            case TXT->Constants.TXT_TRUST;
            case PDF->Constants.PDF_TRUST;
        };
    }
    private double calculateAgreementScore(CanonicalRecord record) {
        int populatedFields=0;
        if(record.getFullName()!=null && !record.getFullName().isBlank()) populatedFields++;
        if(!record.getEmails().isEmpty()) populatedFields++;
        if(!record.getPhones().isEmpty()) populatedFields++;
        if(!record.getSkills().isEmpty()) populatedFields++;
        if(!record.getExperiences().isEmpty()) populatedFields++;
        if(!record.getEducation().isEmpty()) populatedFields++;
        if(populatedFields>=6) return 1.0;
        if(populatedFields>=4) return 0.90;
        if(populatedFields>=2) return 0.75;
        return 0.60;
    }
}
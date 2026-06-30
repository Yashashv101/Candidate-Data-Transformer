package com.yashashv.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@NoArgsConstructor
public class CanonicalRecord {
    private String fullName;
    private String headline;
    private String location;
    private List<String> emails;
    private List<String> phones;
    private List<String> skills;
    private List<Experience> experiences;
    private List<Education> education;
    private double confidence;
    private List<Provenance> provenance;
}
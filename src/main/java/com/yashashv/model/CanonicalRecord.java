package com.yashashv.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
@Getter
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
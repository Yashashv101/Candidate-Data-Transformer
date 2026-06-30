package com.yashashv.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
public class CanonicalRecord {
    private String fullName;
    private String headline;
    private String location;
    private List<String> emails=new ArrayList<>();
    private List<String> phones=new ArrayList<>();
    private List<String> skills=new ArrayList<>();
    private List<Experience> experiences=new ArrayList<>();
    private List<Education> education=new ArrayList<>();
    private double confidence;
    private List<Provenance> provenance=new ArrayList<>();
    private SourceType sourceType;
    private String candidateID;
}
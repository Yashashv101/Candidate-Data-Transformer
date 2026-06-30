package com.yashashv.projection;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Config {
    private List<String> fields;
    private Map<String, String> renameFields;
    private boolean includeConfidence;
    private boolean includeProvenance;
    private String onMissing;
}

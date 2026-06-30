package com.yashashv.normalizer;

import java.util.HashMap;
import java.util.Map;

public class SkillNormalizer {
    private static final Map<String, String> SKILL_MAP = new HashMap<>();
    static {
        SKILL_MAP.put("js", "javascript");
        SKILL_MAP.put("javascript", "javascript");
        SKILL_MAP.put("reactjs", "react");
        SKILL_MAP.put("react.js", "react");
        SKILL_MAP.put("nodejs", "node");
        SKILL_MAP.put("node.js", "node");
        SKILL_MAP.put("spring boot", "spring");
    }
    private SkillNormalizer() {}
    public static String normalize(String skill) {
        if (skill == null) return null;
        skill = skill.trim().toLowerCase();
        if (skill.isBlank()) return null;
        return SKILL_MAP.getOrDefault(skill, skill);
    }
}

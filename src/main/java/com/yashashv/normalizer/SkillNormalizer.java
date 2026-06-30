package com.yashashv.normalizer;

import java.util.HashMap;
import java.util.Map;

public class SkillNormalizer {
    private static final Map<String, String> map = new HashMap<>();
    static {
        map.put("js", "javascript");
        map.put("javascript", "javascript");
        map.put("reactjs", "react");
        map.put("react.js", "react");
        map.put("nodejs", "node");
        map.put("node.js", "node");
        map.put("node", "node");
        map.put("java","java");
        map.put("css","css");
        map.put("docker","docker");
        map.put("spring boot", "spring");
    }
    private SkillNormalizer() {}
    public static String normalize(String skill) {
        if (skill == null) return null;
        skill = skill.trim().toLowerCase();
        if (skill.isBlank()) return null;
        return map.getOrDefault(skill, skill);
    }
}

package com.yashashv.parser;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.SourceType;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PdfParser implements Parser {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+?\\d[\\d\\s\\-]{8,14}");
    private static final List<String> SECTION_KEYWORDS = List.of(
        "education", "experience", "projects", "achievements",
        "skills", "certifications", "summary", "objective", "publications"
    );
    @Override
    public List<CanonicalRecord> parse(String filePath) throws IOException {
        CanonicalRecord record = new CanonicalRecord();
        try (PDDocument doc = Loader.loadPDF(new File(filePath))) {
            String text = new PDFTextStripper().getText(doc);
            extractFields(record, text);
        }
        record.setSourceType(SourceType.PDF);
        return List.of(record);
    }
    private void extractFields(CanonicalRecord record, String text) {
        String[] lines = text.split("\\r?\\n");
        boolean inSkillsSection = false;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (isSectionHeader(line)) {
                inSkillsSection = line.toLowerCase().contains("skill");
                continue;
            }
            if (inSkillsSection) {
                extractSkillsFromLine(record, line);
                continue;
            }
            if (record.getFullName() == null && !line.matches(".*\\d.*") && line.split("\\s+").length <= 5) {
                record.setFullName(line);
                continue;
            }
            Matcher emailMatcher = EMAIL_PATTERN.matcher(line);
            while (emailMatcher.find()) {
                record.getEmails().add(emailMatcher.group());
            }
            Matcher phoneMatcher = PHONE_PATTERN.matcher(line);
            while (phoneMatcher.find()) {
                String phone = phoneMatcher.group().trim();
                if (phone.replaceAll("[^\\d]", "").length() >= 8) {
                    record.getPhones().add(phone);
                }
            }
            if (record.getLocation() == null && line.contains("|") && line.contains("@")) {
                String location = line.split("\\|")[0].trim();
                if (!location.isBlank()) record.setLocation(location);
            }
        }
    }
    private boolean isSectionHeader(String line) {
        if (line.isEmpty() || !Character.isUpperCase(line.charAt(0))) return false;
        if (line.length() > 40) return false;
        String lower = line.toLowerCase();
        return SECTION_KEYWORDS.stream().anyMatch(k -> lower.equals(k) || lower.startsWith(k) || lower.endsWith(k));
    }
    private void extractSkillsFromLine(CanonicalRecord record, String line) {
        line = line.replaceAll("^[•\\-*\\u2022]+\\s*", "").trim();
        if (line.isBlank()) return;
        if (line.contains(":")) {
            line = line.substring(line.indexOf(':') + 1).trim();
        }
        Arrays.stream(line.split(",")).map(String::trim).filter(s -> !s.isBlank()).forEach(record.getSkills()::add);
    }
}
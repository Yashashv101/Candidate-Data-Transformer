package com.yashashv.normalizer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateNormalizer {
    private DateNormalizer() {}
    public static String normalize(String date) {
        if (date == null) return null;
        date = date.trim();
        if (date.isBlank()) return null;
        if (date.equalsIgnoreCase("Present")||date.equalsIgnoreCase("Current")) return "Present";
        try {
            LocalDate d = LocalDate.parse(date);
            return YearMonth.from(d).toString();
        } catch (Exception e) {}
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            YearMonth ym = YearMonth.parse(date, formatter);
            return ym.toString();
        } catch (Exception e) {}
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
            YearMonth ym = YearMonth.parse(date, formatter);
            return ym.toString();
        } catch (Exception e) {}
        return null;
    }
}

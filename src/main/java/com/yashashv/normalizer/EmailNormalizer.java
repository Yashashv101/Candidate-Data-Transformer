package com.yashashv.normalizer;

public class EmailNormalizer {
    public static String normalize(String email) {
        if(email == null) return null;
        email = email.trim().toLowerCase();
        return email.isBlank() ? null : email;
    }
}

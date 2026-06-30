package com.yashashv.normalizer;

public class PhoneNormalizer {
    private PhoneNormalizer() {}
    public static String normalize(String phone) {
        if(phone==null) return null;
        phone=phone.trim();
        phone=phone.replaceAll("[^0-9]", "");
        if(phone.isBlank()) return null;
        if(phone.length()==10) return "+91" + phone;
        if(phone.length()==12 && phone.startsWith("91")) return "+" + phone;
        return phone;
    }
}

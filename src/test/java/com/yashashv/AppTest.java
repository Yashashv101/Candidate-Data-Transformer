package com.yashashv;

import com.yashashv.model.CanonicalRecord;
import com.yashashv.normalizer.EmailNormalizer;
import com.yashashv.normalizer.PhoneNormalizer;
import com.yashashv.normalizer.SkillNormalizer;
import com.yashashv.normalizer.DateNormalizer;
import com.yashashv.matcher.Matcher;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    // --- Normalizer tests ---

    @Test
    void emailNormalizerLowercasesAndTrims() {
        assertEquals("alice@example.com", EmailNormalizer.normalize("  Alice@Example.COM  "));
    }

    @Test
    void phoneNormalizerFormatsIndianNumber() {
        assertEquals("+919876543210", PhoneNormalizer.normalize("9876543210"));
    }

    @Test
    void phoneNormalizerPreservesAlreadyFormattedNumber() {
        assertEquals("+919876543210", PhoneNormalizer.normalize("+919876543210"));
    }

    @Test
    void skillNormalizerCanonicalizesAlias() {
        assertEquals("javascript", SkillNormalizer.normalize("js"));
        assertEquals("react", SkillNormalizer.normalize("ReactJS"));
    }

    @Test
    void dateNormalizerConvertsToYearMonth() {
        assertEquals("2023-06", DateNormalizer.normalize("06/2023"));
        assertEquals("Present", DateNormalizer.normalize("Present"));
        assertNull(DateNormalizer.normalize("not-a-date"));
    }

    // --- Matcher deduplication test (edge case: same email from two sources) ---

    @Test
    void matcherGroupsRecordsByEmail() {
        CanonicalRecord r1 = new CanonicalRecord();
        r1.getEmails().add("alice@example.com");
        r1.setFullName("Alice");

        CanonicalRecord r2 = new CanonicalRecord();
        r2.getEmails().add("alice@example.com");
        r2.setFullName("Alice Johnson");

        CanonicalRecord r3 = new CanonicalRecord();
        r3.getEmails().add("bob@example.com");
        r3.setFullName("Bob");

        List<List<CanonicalRecord>> groups = new Matcher().match(List.of(r1, r2, r3));

        // Alice's two records should be in one group; Bob alone in another
        assertEquals(2, groups.size());
        boolean aliceGroupFound = groups.stream().anyMatch(g -> g.size() == 2);
        assertTrue(aliceGroupFound, "Expected Alice's two records to be grouped together");
    }

    // --- Edge case: records with no email but same name+phone are grouped ---

    @Test
    void matcherGroupsByNameAndPhoneWhenNoEmail() {
        CanonicalRecord r1 = new CanonicalRecord();
        r1.setFullName("Carol Williams");
        r1.getPhones().add("+919988776655");

        CanonicalRecord r2 = new CanonicalRecord();
        r2.setFullName("Carol Williams");
        r2.getPhones().add("+919988776655");

        List<List<CanonicalRecord>> groups = new Matcher().match(List.of(r1, r2));
        assertEquals(1, groups.size());
        assertEquals(2, groups.get(0).size());
    }

    // --- Edge case: record with no email and no phone gets its own unmatched group ---

    @Test
    void matcherPutsUnidentifiableRecordInOwnGroup() {
        CanonicalRecord r1 = new CanonicalRecord();
        r1.setFullName("Unknown Person");
        // no email, no phone

        List<List<CanonicalRecord>> groups = new Matcher().match(List.of(r1));
        assertEquals(1, groups.size());
    }
}

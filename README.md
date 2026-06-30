# Candidate Transformer

**Demo video:** [](https://youtu.be/TODO) <!-- replace with your actual link -->


Multi-source candidate data transformer for the Eightfold Engineering Intern assignment.

Reads candidate data from CSV, JSON, and TXT files, deduplicates and merges records, and emits a clean canonical JSON profile per candidate.

---

## How to run

**Requirements:** Java 21, Maven 3.x

### Build
```bash
mvn clean package -DskipTests
```

### Run with default config and sample inputs
```bash
java -jar target/candidate-transformer-1.0-SNAPSHOT.jar
```
This reads `config.json` and `samples/recruiter.csv`, `samples/candidate.json`, `samples/notes.txt`, and writes `output.json`.

### Run with a custom config
```bash
java -jar target/candidate-transformer-1.0-SNAPSHOT.jar custom_config.json samples/recruiter.csv samples/candidate.json samples/notes.txt
```

### Run tests
```bash
mvn test
```

The test suite covers 8 cases across normalizers and the matcher:

| Test | What it checks |
|------|----------------|
| `emailNormalizerLowercasesAndTrims` | Emails are lowercased and whitespace-stripped |
| `phoneNormalizerFormatsIndianNumber` | 10-digit number gets `+91` prefix |
| `phoneNormalizerPreservesAlreadyFormattedNumber` | `+91XXXXXXXXXX` is not double-prefixed |
| `skillNormalizerCanonicalizesAlias` | `js` → `javascript`, `ReactJS` → `react` |
| `dateNormalizerConvertsToYearMonth` | `06/2023` → `2023-06`, `Present` preserved, garbage → `null` |
| `matcherGroupsRecordsByEmail` | Two records with the same email are merged into one group |
| `matcherGroupsByNameAndPhoneWhenNoEmail` | Falls back to name + phone when email is absent |
| `matcherPutsUnidentifiableRecordInOwnGroup` | Record with no email and no phone is kept, not dropped |

---

## Pipeline

```
Parse → Normalize → Match → Merge → Confidence → Project → Validate → output.json
```

1. **Parse** — detects file type by extension and reads into `CanonicalRecord` objects
2. **Normalize** — emails (lowercase/trim), phones (E.164 +91), skills (canonical names), dates (YYYY-MM)
3. **Match** — groups records by email; falls back to name + phone; unidentifiable records are kept as-is
4. **Merge** — picks the highest-trust source (CSV > JSON > TXT) for scalar fields; unions lists
5. **Confidence** — scores each merged record based on source trust and field completeness
6. **Project** — applies runtime config: field selection, renaming, include/exclude confidence & provenance
7. **Validate** — checks required fields and `on_missing` policy before writing output

---

## Config format

```json
{
  "fields": ["candidateId", "fullName", "emails", "phones", "skills", "headline", "location"],
  "renameFields": { "candidateId": "id" },
  "includeConfidence": true,
  "includeProvenance": false,
  "onMissing": "omit"
}
```

| Key | Description |
|-----|-------------|
| `fields` | Which canonical fields to include in output |
| `renameFields` | Rename a field in the output (key = canonical name, value = output name) |
| `includeConfidence` | Append `confidence` score to each record |
| `includeProvenance` | Append `provenance` audit trail to each record |
| `onMissing` | `"omit"` (skip missing fields) or `"error"` (fail on missing) |

---

## Supported source types

| Source | Type | Fields read |
|--------|------|-------------|
| `recruiter.csv` | Structured | full_name, email, phone, headline, location |
| `candidate.json` | Structured (ATS blob) | full_name, email, phone, headline, location |
| `notes.txt` | Unstructured | Name, Email, Phone, Headline, Location, Skills |
| `resume.pdf` | Unstructured | Extracted via PDFBox — email/phone via regex, skills via section detection |

---

## Assumptions & descoped items

- Phone normalization assumes India (+91). International formats are left as-is.
- `location` is stored as a flat string. Splitting into city/region/country would require a geo API or heuristic parsing — descoped for time.
- `skills` confidence per skill (vs. per record) was descoped; the overall record confidence covers this.
- LinkedIn/GitHub URL parsing was descoped; the TXT parser covers the unstructured source requirement.
- Education parsing from unstructured text was descoped; the model supports it if a structured source provides it.

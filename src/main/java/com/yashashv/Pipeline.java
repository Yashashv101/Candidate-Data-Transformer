package com.yashashv;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yashashv.confidence.ConfidenceCalc;
import com.yashashv.matcher.Matcher;
import com.yashashv.merger.Merger;
import com.yashashv.model.CanonicalRecord;
import com.yashashv.model.SourceType;
import com.yashashv.normalizer.RecordCleaner;
import com.yashashv.parser.*;
import com.yashashv.projection.Config;
import com.yashashv.projection.Projector;
import com.yashashv.validator.Validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Pipeline {
    private final RecordCleaner cleaner = new RecordCleaner();
    private final Matcher matcher = new Matcher();
    private final Merger merger = new Merger();
    private final ConfidenceCalc confidenceCalculator = new ConfidenceCalc();
    private final Projector projector = new Projector();
    private final Validator validator = new Validator();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public void execute(List<String> inputFiles, Config config, String outputFile) throws IOException {
        List<CanonicalRecord> records = new ArrayList<>();
        for (String file : inputFiles) {
            Parser parser = getParser(SourceDetector.detect(file));
            List<CanonicalRecord> parsedRecords = parser.parse(file);
            for (CanonicalRecord record : parsedRecords) {
                records.add(cleaner.clean(record));
            }
        }
        List<List<CanonicalRecord>> groups = matcher.match(records);
        List<CanonicalRecord> mergedRecords = merger.merge(groups);
        confidenceCalculator.calculate(mergedRecords);
        List<Map<String, Object>> projectedRecords = projector.project(mergedRecords, config);
        validator.validate(projectedRecords, config);
        objectMapper.writeValue(new File(outputFile), projectedRecords);
    }
    private Parser getParser(SourceType sourceType) {
        return switch (sourceType) {
            case CSV -> new CsvParser();
            case JSON -> new JsonParser();
            case TXT -> new TextParser();
        };
    }
}

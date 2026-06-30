package com.yashashv.parser;
import com.yashashv.model.CanonicalRecord;
import java.io.IOException;
import java.util.List;

public interface Parser {
    List<CanonicalRecord> parse(String fPath) throws IOException;
}

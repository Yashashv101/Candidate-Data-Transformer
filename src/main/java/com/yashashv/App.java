package com.yashashv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashashv.projection.Config;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) throws IOException {
        // Usage: java -jar app.jar [config.json] [input1 input2 ...]
        // Defaults are used if no arguments provided.
        String configPath = "config.json";
        List<String> inputFiles = List.of(
            "samples/recruiter.csv",
            "samples/candidate.json",
            "samples/notes.txt", "samples/YashashvCV.pdf"
        );
        if (args.length >= 1) configPath=args[0];
        if (args.length >= 2) inputFiles=Arrays.asList(Arrays.copyOfRange(args,1,args.length));
        try {
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new File(configPath), Config.class);
            Pipeline pipeline = new Pipeline();
            pipeline.execute(inputFiles, config, "output.json");
            System.out.println("Done. Output written to output.json");
        } catch (Exception e) {
            System.err.println("Transformation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

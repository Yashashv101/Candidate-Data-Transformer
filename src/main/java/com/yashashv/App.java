package com.yashashv;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yashashv.parser.*;
import com.yashashv.projection.Config;
import java.io.IOException;
import java.util.List;
public class App 
{
    public static void main( String[] args ) throws IOException {
        try {
            List<String> inputFiles = List.of(
                    "samples/recruiter.csv",
                    "samples/candidate.json",
                    "samples/notes.txt"
            );
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new java.io.File("config.json"), Config.class);
            Pipeline pipeline = new Pipeline();
            pipeline.execute(inputFiles, config, "output.json");
            System.out.println("Transformation completed successfully.");
        } catch (Exception e) {
            System.out.println("Transformation failed.");
            e.printStackTrace();
        }
    }
}

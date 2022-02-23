package com.dnadra.jpdfmerge.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JPdfMergeTest {
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.Test
    void mergePdfs() {
        JPdfMerge merge = new JPdfMerge();
        merge.addInputPdf("src/test/resources/1.pdf");
        merge.addInputPdf("src/test/resources/2.pdf");
        merge.setOutputPdf("src/test/resources/merged.pdf");
        Path outputFilePath = Paths.get("src/test/resources/merged.pdf");
        try {
            if (Files.exists(outputFilePath)) Files.delete(outputFilePath);
        } catch (IOException e) {
            assertTrue(false, "Failed to delete output file. Exception was thrown: " + e);
        }
        try {
            merge.mergePdfs();
            assertTrue(Files.exists(outputFilePath));
        } catch (IOException e) {
            assertTrue(false, "Exception was thrown: " + e);
        }
    }
}
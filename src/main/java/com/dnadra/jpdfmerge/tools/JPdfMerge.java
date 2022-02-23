package com.dnadra.jpdfmerge.tools;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JPdfMerge {
    private final List<String> inputPdfs = new ArrayList<>();
    private String outputPdf;

    public void addInputPdf(String path) {
        this.inputPdfs.add(path);
    }

    public void removeInputPdf(String path) {
        this.inputPdfs.remove(path);
    }

    public void clearInputPdfs() {
        this.inputPdfs.clear();
    }

    public void setOutputPdf(String path) {
        this.outputPdf = path;
    }

    public void mergePdfs() throws IOException {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        for (String path : this.inputPdfs) {
            File pdfFile = new File(path);
            mergerUtility.addSource(pdfFile);
            mergerUtility.setDocumentMergeMode(PDFMergerUtility.DocumentMergeMode.PDFBOX_LEGACY_MODE);
        }
        mergerUtility.setDestinationFileName(this.outputPdf);
        mergerUtility.mergeDocuments(null);
    }
}

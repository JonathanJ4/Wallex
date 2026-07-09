package com.wallex.controller;

import java.awt.PageAttributes;
import java.util.*;

import com.wallex.dto.PdfPreviewResponse;
import com.wallex.service.PdfImportService;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.wallex.dto.PdfImportResponse;
import com.wallex.entity.Transaction;

@RestController
@RequestMapping("/transactions/import/pdf")
@CrossOrigin(origins = "http://localhost:5173")
public class PdfImportController {

    private final PdfImportService pdfImportService;

    public PdfImportController(PdfImportService pdfImportService) {
        this.pdfImportService = pdfImportService;
    }

    @PostMapping(
            value = "/preview",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public PdfPreviewResponse previewPdf(
            @RequestParam("file") MultipartFile file
    ) {
        return pdfImportService.extractText(file);
    }

    @PostMapping(value  = "/import", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public PdfImportResponse importPdf( @RequestParam ("file") MultipartFile file){
        List<Transaction> imported = pdfImportService.importTransactions(file);
        return new PdfImportResponse(imported);
    }
}
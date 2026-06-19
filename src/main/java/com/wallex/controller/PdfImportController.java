package com.wallex.controller;

import com.wallex.dto.PdfPreviewResponse;
import com.wallex.service.PdfImportService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/transactions/import/pdf")
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
}
package com.wallex.dto;

public record PdfPreviewResponse(
        String fileName,
        int pageCount,
        String extractedText
) {
}

package com.wallex.service;

import com.wallex.dto.PdfPreviewResponse;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


@Service 
public class PdfImportService{
    public PdfPreviewResponse extractText(MultipartFile file){
        validatePdf(file);
    

        try(PDDocument document = Loader.loadPDF(file.getBytes())){

        PDFTextStripper textStripper = new PDFTextStripper();
        String extractedText = textStripper.getText(document);
        return new PdfPreviewResponse(file.getOriginalFilename(), document.getNumberOfPages(),extractedText);


    }catch(IOException exception){
        throw new IllegalArgumentException( "Unable to read the file gangy", exception);
    }

    }

    private void validatePdf(MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("Yo the file cannot be empty twin");
        }
        String filename = file.getOriginalFilename();
        String contentType = file.getContentType();

        boolean hasPdfExtension = filename != null & filename.toLowerCase().endsWith("pdf");
        boolean hasPdfContentType =
                "application/pdf".equalsIgnoreCase(contentType);
        if(!hasPdfExtension || !hasPdfContentType){
            throw new IllegalArgumentException("Only pdf files allowed twin");
        } 
    }


}
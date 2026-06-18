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
        validatedPdf(file);
    

        try(PDDocument document = Loader.loadPDF(file.getBytes())){

        PDFTextStripper textStripper = new PDFTextStripper();
        String extractedText = textStripper.getText(document);
        return new PdfPreviewResponse(file.getOriginalFilename(), document.getNumberOfPages(),extractedText);


    }catch(IOException exception){
        throw new Ill
    }

    }




}
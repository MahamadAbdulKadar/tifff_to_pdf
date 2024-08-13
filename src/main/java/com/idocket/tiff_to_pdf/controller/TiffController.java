package com.idocket.tiff_to_pdf.controller;

import com.idocket.tiff_to_pdf.type.ContentType;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tiff")
public class TiffController {
    @PostMapping("/to-pdf")
    public String ConvertTiffToPDF(@RequestParam("file") MultipartFile file,@RequestParam("folder") String folder) throws IOException {

        String fileExtension = file.getContentType();
        if(!fileExtension.equalsIgnoreCase(ContentType.TIFF.getContentType()) && !fileExtension.equalsIgnoreCase(ContentType.TIF.getContentType())){
            return "File Format Not Supported : " + fileExtension;
        }

        PDDocument document = new PDDocument();
        String fileName = file.getOriginalFilename().replaceFirst("[.][^.]+$", "") + ".pdf";
        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, file.getBytes(), null);
        int imageWidth = pdImage.getWidth();
        int imageHeight = pdImage.getHeight();
        PDPage page;
        if (imageWidth > imageHeight) {
            page = new PDPage(new PDRectangle(imageWidth, imageHeight));
        } else {
            page = new PDPage(new PDRectangle(imageHeight, imageWidth));
        }
        document.addPage(page);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(),
                    page.getMediaBox().getHeight());
        }
        document.save(folder + "/" + fileName);
        return "You successfully converted the file and stored at  " + folder + "/" + fileName + " !";
    }
}

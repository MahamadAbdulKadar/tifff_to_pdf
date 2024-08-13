package com.idocket.tiff_to_pdf.service;

import com.idocket.tiff_to_pdf.service.TiffToPdfService;
import com.idocket.tiff_to_pdf.type.ContentType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class TiffToPdfService {
    public String convertTiffToPDF(MultipartFile file, String folder) throws IOException, ImageReadException {
        String fileExtension = file.getContentType();
        if(!fileExtension.equalsIgnoreCase(ContentType.TIFF.getContentType()) && !fileExtension.equalsIgnoreCase(ContentType.TIF.getContentType())){
            return "File Format Not Supported : " + fileExtension;
        }

        PDDocument document = new PDDocument();
        String fileName = file.getOriginalFilename().replaceFirst("[.][^.]+$", "") + ".pdf";

        BufferedImage[] images = Imaging.getAllBufferedImages(file.getBytes()).toArray(new BufferedImage[0]);
        for (int i = 0; i < images.length; i++) {
            BufferedImage image = images[i];
            System.out.println("Image " + (i + 1) + ": " + image.getWidth() + "x" + image.getHeight());

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, convertBufferedImageToByteArray(image,"png"), null);
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
        }

        document.save(folder + "/" + fileName);
        return "You successfully converted the file and stored at  " + folder + "/" + fileName + " !";
    }

    private byte[] convertBufferedImageToByteArray(BufferedImage image, String format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, format, baos);
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

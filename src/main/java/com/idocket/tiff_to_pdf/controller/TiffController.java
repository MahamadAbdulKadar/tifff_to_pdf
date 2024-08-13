package com.idocket.tiff_to_pdf.controller;

import com.idocket.tiff_to_pdf.service.TiffToPdfService;
import lombok.AllArgsConstructor;
import org.apache.commons.imaging.ImageReadException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/tiff")
public class TiffController {
    private TiffToPdfService tiffToPdfService;
    @PostMapping("/to-pdf")
    public String ConvertTiffToPDF(@RequestParam("file") MultipartFile file,@RequestParam("folder") String folder) throws IOException, ImageReadException {
        return tiffToPdfService.convertTiffToPDF(file,folder);
    }
}

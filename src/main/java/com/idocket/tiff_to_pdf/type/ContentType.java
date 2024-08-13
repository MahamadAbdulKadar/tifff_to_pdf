package com.idocket.tiff_to_pdf.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContentType {
    TIFF("image/tiff"),
    TIF("image/tif");

    private final String contentType;
}

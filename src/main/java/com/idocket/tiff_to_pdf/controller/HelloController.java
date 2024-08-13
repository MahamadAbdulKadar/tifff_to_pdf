package com.idocket.tiff_to_pdf.controller;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@AllArgsConstructor
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping()
    public String index() {
        return "Greetings from Spring Boot!";
    }
}

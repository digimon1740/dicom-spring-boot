package com.example.viewer.demo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class ViewerController {

    //GET localhost:8080/image?filename=1545889291487.jpg
    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(@RequestParam("filename") String filename, HttpServletResponse response) throws IOException {

        ClassPathResource imgFile = new ClassPathResource("image/" + filename);


        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        try {
            StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
        } catch (Exception e) {
            String path = imgFile.getPath();
            StreamUtils.copy(new FileInputStream(new File(path)), response.getOutputStream());
        }
    }

    //POST localhost:8080/convert
    @PostMapping(value = "/convert")
    public ResponseEntity<String> convertImage() throws IOException {
        String filename = "{dcm4che}"; // dcm4che/bin/dcm2jpg
        String orgFile = "{dcmFile}"; // dcmFile 경로

        ClassPathResource img = new ClassPathResource("image/");
        String newFilename = System.currentTimeMillis() + ".jpg";

        String destFile = img.getPath() + newFilename;

        String[] exec = new String[3];
        exec[0] = filename;
        exec[1] = orgFile;
        exec[2] = destFile;
        Runtime.getRuntime().exec(exec);

        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(newFilename);
    }
}

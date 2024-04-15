package com.gmail.controller;

import com.gmail.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.gmail.constants.PathConstants.API_V1_IMAGE;
import static com.gmail.constants.PathConstants.UPLOAD;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_IMAGE)
public class ImageController {

    private final ImageService imageService;

    @PostMapping(UPLOAD)
    public String uploadImage(@RequestPart("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }
}

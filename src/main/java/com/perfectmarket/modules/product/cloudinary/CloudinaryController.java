package com.perfectmarket.modules.product.cloudinary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    public ResponseEntity<Map<String, Object>> getUploadSignature(@RequestParam String folderName) {
        return ResponseEntity.ok(cloudinaryService.getSignature(folderName));
    }

}

package com.perfectmarket.modules.product.cloudinary;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<Map<String, Object>> getUploadSignature(@RequestParam String folderName) {
        return ResponseEntity.ok(cloudinaryService.getSignature(folderName));
    }

}

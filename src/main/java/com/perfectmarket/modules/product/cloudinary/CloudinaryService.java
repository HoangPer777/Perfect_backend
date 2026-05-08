package com.perfectmarket.modules.product.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<String, Object> getSignature(String folderName) {
        // 1. Tạo timestamp (đơn vị giây)
        long timestamp = System.currentTimeMillis() / 1000L;

        // 2. Định nghĩa các tham số cần "ký tên"
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", folderName);

        // 3. Tạo Signature dùng API Secret
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

        // 4. Trả về thông tin cho Client
        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("cloud_name", cloudinary.config.cloudName);
        response.put("api_key", cloudinary.config.apiKey);
        response.put("folder", folderName);

        return response;
    }
}
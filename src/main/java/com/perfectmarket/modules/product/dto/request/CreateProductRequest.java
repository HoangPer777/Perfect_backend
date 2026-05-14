package com.perfectmarket.modules.product.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateProductRequest(String title, String description,
                                   String thumbnailUrl, String status, List<String> images, List<UUID> categories) { }

-- 1. Chèn Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_CUSTOMER'), (2, 'ROLE_DESIGNER'), (3, 'ROLE_ADMIN')
ON CONFLICT (id) DO NOTHING;

-- 2. Chèn Users mẫu
INSERT INTO users (id, email, full_name, password_hash, provider, status, created_at, updated_at) VALUES
    ('91717078-9da1-40f9-b1d6-922e3f561e8d', 'anguyen@gmail.com', 'Nguyen Van A', '$2a$10$NCMJuR7onGNb0wdYwMRJw.2SXP6i1R/oQbqLBmCA.1F9LnTx3A7TW', 'LOCAL', 'ACTIVE', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- 3. Chèn User Roles
INSERT INTO user_roles (user_id, role_id) VALUES ('91717078-9da1-40f9-b1d6-922e3f561e8d', 2)
ON CONFLICT DO NOTHING;

-- 4. Chèn Categories
INSERT INTO categories (id, name, slug) VALUES
                                            ('cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'Video Editing', 'video-editing'),
                                            ('512e8b56-b512-45ed-8e98-2185eeb20ace', 'Photo & Retouching', 'photo-and-retouching')
ON CONFLICT (id) DO NOTHING;

-- 5. Chèn Products (Đã cập nhật giá > 500.000)
INSERT INTO public.products (id, designer_id, title, description, price, status, is_active, thumbnail_url, rating_avg, sold_count, view_count, created_at, updated_at) VALUES
                                                                                                                                                                        ('cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Professional E-Commerce Product Retouching & Color Correction Pack', 'High-end product retouching system.', 850000, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643392/designer-projects/ynjve7gw41cqmmjxyrsm.avif', 0, 0, 0, NOW(), NOW()),
                                                                                                                                                                        ('810463cc-bbb4-4eaf-9022-83d564e07538', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'High-Converting Social Media Banner Kit for Fashion', 'A dynamic set of promotional banners for fashion brands.', 550000, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643435/designer-projects/cp0tshkux8opemueiafz.avif', 0, 0, 0, NOW(), NOW()),
                                                                                                                                                                        ('a1fab818-cc4c-434e-be79-1acb85c37f8f', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Minimalist Web Hero Banner Design for Tech Store', 'Sleek Hero Banner template for modern tech websites.', 720000, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643486/designer-projects/phazh2ke6p13ayrvlxwp.jpg', 0, 0, 0, NOW(), NOW()),
                                                                                                                                                                        ('a22e27d4-9824-4416-ba55-13db340c248c', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Vintage Photo Restoration & Cinematic Grading System', 'Professional photo restoration bundle with cinematic presets.', 1200000, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643520/designer-projects/thifmmhjssr1pl5pdp5e.avif', 0, 0, 0, NOW(), NOW()),
                                                                                                                                                                        ('aadcbfd9-56b6-4bb0-b879-f59968d164e5', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Abstract Neon Event Banner & Typography Template', 'Eye-catching event banner template with neon style.', 645000, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643551/designer-projects/g1bx6amqiljrt2htozlk.avif', 0, 0, 0, NOW(), NOW())
ON CONFLICT (id) DO UPDATE SET
                               price = EXCLUDED.price,
                               updated_at = NOW();

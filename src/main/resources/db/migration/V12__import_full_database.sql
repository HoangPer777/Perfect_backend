-- V5__import_full_database.sql
-- Tắt kiểm tra FK tạm thời để chèn dữ liệu an toàn
SET session_replication_role = 'replica';

-- 1. Chèn Categories
INSERT INTO public.categories (id, parent_id, icon, name, slug) VALUES
                                                                    ('cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', NULL, 'video-camera', 'Video Editing', 'video-editing'),
                                                                    ('512e8b56-b512-45ed-8e98-2185eeb20ace', NULL, 'camera', 'Photo & Retouching', 'photo-and-retouching'),
                                                                    ('5ddaa464-c13b-4a07-a4be-14e574cd0759', NULL, 'film', 'Motion Graphics', 'motion-graphics'),
                                                                    ('7108b700-d865-445f-acfb-d1be69434917', NULL, 'layers', 'Graphic Assets', 'graphic-assets'),
                                                                    ('7e58a042-9b78-4f08-8322-a2eb06256771', NULL, 'volume-up', 'Audio & Sound', 'audio-and-sound'),
                                                                    ('9f3159e2-1aca-4d8f-9676-6bf5aa7df6ed', 'cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'layout', 'CapCut Templates', 'capcut-templates'),
                                                                    ('e314e286-00cb-4c1f-909c-60a2b00e8eda', 'cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'adobe', 'Premiere Pro Project Files', 'premiere-pro-projects'),
                                                                    ('9d978aa4-6181-414e-a7a7-b56b27b6855d', 'cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'palette', 'LUTs & Color Grading', 'luts-and-color-grading'),
                                                                    ('07462123-e78e-4d98-8851-9c48cd6b93d7', 'cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'transform', 'Video Transitions', 'video-transitions'),
                                                                    ('6fd4ab91-ded6-4418-93f0-d06e59490cd1', 'cb4e27b3-e83b-4e64-8fb7-9cc0edf8d209', 'aperture', 'DaVinci Resolve Macros', 'davinci-resolve-macros'),
                                                                    ('6bbe712f-814a-441c-8fed-be748f440805', '512e8b56-b512-45ed-8e98-2185eeb20ace', 'sliders', 'Lightroom Presets (.xmp/.dng)', 'lightroom-presets'),
                                                                    ('a139db66-5879-4352-a1c9-deea8b8eac98', '512e8b56-b512-45ed-8e98-2185eeb20ace', 'play-circle', 'Photoshop Actions (.atn)', 'photoshop-actions'),
                                                                    ('eba893b4-f292-4fb0-8f20-21fa8e56a296', '512e8b56-b512-45ed-8e98-2185eeb20ace', 'image', 'Camera Raw Overlays', 'camera-raw-overlays'),
                                                                    ('163196bc-52ec-4728-b656-fd28ab6b5565', '512e8b56-b512-45ed-8e98-2185eeb20ace', 'scissors', 'Background Removal Assets', 'background-removal-assets'),
                                                                    ('7aa54d33-137e-4f90-a955-ac7ee5db759f', '5ddaa464-c13b-4a07-a4be-14e574cd0759', 'activity', 'After Effects Templates', 'after-effects-templates'),
                                                                    ('471a2176-f6d7-4856-8ca1-01d2e0f55d87', '5ddaa464-c13b-4a07-a4be-14e574cd0759', 'text', 'Animated Titles & Typography', 'animated-titles-typography'),
                                                                    ('6505cce3-6ee1-4186-bf74-8f9cb89664e5', '5ddaa464-c13b-4a07-a4be-14e574cd0759', 'tv', 'Stream Overlay Packs (Twitch/YT)', 'stream-overlays'),
                                                                    ('a1599bbf-af0b-4b43-8d35-229b6e032b1f', '5ddaa464-c13b-4a07-a4be-14e574cd0759', 'star', 'Logo Reveals & Intros', 'logo-reveals-intros'),
                                                                    ('faf121ee-a349-456f-ace5-f95966954a5a', '7108b700-d865-445f-acfb-d1be69434917', 'instagram', 'Social Media Templates (Canva/PSD)', 'social-media-templates'),
                                                                    ('d206377b-a330-41bb-96d9-41841917f3fe', '7108b700-d865-445f-acfb-d1be69434917', 'vector', 'Vector Illustrations', 'vector-illustrations'),
                                                                    ('684d5489-e8e8-43b5-ac20-a5099afa6d1d', '7108b700-d865-445f-acfb-d1be69434917', 'figma', 'UI/UX Kits (Figma)', 'ui-ux-kits-figma'),
                                                                    ('e40ae637-b066-47a6-8777-2b83df8265e2', '7108b700-d865-445f-acfb-d1be69434917', 'font', 'Custom Fonts & Typography', 'custom-fonts'),
                                                                    ('d300d9df-92f1-43c4-affe-5e866f3a1792', '7e58a042-9b78-4f08-8322-a2eb06256771', 'music', 'Sound Effects (SFX)', 'sound-effects-sfx'),
                                                                    ('430c1dde-7112-4589-ba30-360823bc1178', '7e58a042-9b78-4f08-8322-a2eb06256771', 'disc', 'Background Music Tracks', 'background-music'),
                                                                    ('7590ef1e-01f1-4673-8822-94b6bae14f6f', '7e58a042-9b78-4f08-8322-a2eb06256771', 'bell', 'Intro & Outro Audio Jingles', 'audio-jingles')
ON CONFLICT (id) DO NOTHING;

-- 2. Chèn Users
INSERT INTO public.users (id, email, full_name, password_hash, status, username, is_verified, created_at, updated_at) VALUES
    ('91717078-9da1-40f9-b1d6-922e3f561e8d', 'anguyen@gmail.com', 'Nguyen Van A', '$2a$10$NCMJuR7onGNb0wdYwMRJw.2SXP6i1R/oQbqLBmCA.1F9LnTx3A7TW', 'ACTIVE', 'anguyen@gmail.com', false, '2026-05-25 00:12:22.600879+07', '2026-05-25 00:12:22.600879+07')
ON CONFLICT (id) DO NOTHING;

-- 3. Chèn User_Roles
INSERT INTO public.user_roles (user_id, role_id) VALUES
    ('91717078-9da1-40f9-b1d6-922e3f561e8d', 2)
ON CONFLICT (user_id, role_id) DO NOTHING;

-- 4. Chèn Products
INSERT INTO public.products (id, designer_id, title, description, price, status, is_active, thumbnail_url, created_at, updated_at) VALUES
                                                                                                                                       ('cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Professional E-Commerce Product Retouching & Color Correction Pack', 'High-end product retouching system.', 0, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643392/designer-projects/rmqrnv9rdvpeq9ucywqq.avif', '2026-05-25 00:23:13.3039', '2026-05-25 00:23:13.3039'),
                                                                                                                                       ('810463cc-bbb4-4eaf-9022-83d564e07538', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'High-Converting Social Media Banner Kit for Fashion', 'A dynamic set of promotional banners...', 0, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643435/designer-projects/cp0tshkux8opemueiafz.avif', '2026-05-25 00:23:57.237464', '2026-05-25 00:23:57.237464'),
                                                                                                                                       ('a1fab818-cc4c-434e-be79-1acb85c37f8f', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Minimalist Web Hero Banner Design for Tech Store', 'Sleek Hero Banner template...', 0, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643486/designer-projects/phazh2ke6p13ayrvlxwp.jpg', '2026-05-25 00:24:46.773185', '2026-05-25 00:24:46.773185'),
                                                                                                                                       ('a22e27d4-9824-4416-ba55-13db340c248c', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Vintage Photo Restoration & Cinematic Grading System', 'Professional photo restoration bundle...', 0, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643520/designer-projects/thifmmhjssr1pl5pdp5e.avif', '2026-05-25 00:25:20.969791', '2026-05-25 00:25:20.969791'),
                                                                                                                                       ('aadcbfd9-56b6-4bb0-b879-f59968d164e5', '91717078-9da1-40f9-b1d6-922e3f561e8d', 'Abstract Neon Event Banner & Typography Template', 'Eye-catching event banner template...', 0, 'PUBLISHED', TRUE, 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643551/designer-projects/g1bx6amqiljrt2htozlk.avif', '2026-05-25 00:25:52.502848', '2026-05-25 00:25:52.502848')
ON CONFLICT (id) DO NOTHING;

-- 5. Chèn Product_Images
INSERT INTO public.product_images (id, product_id, url, status, created_at) VALUES
                                                                                ('f387e856-f7ba-472c-9f08-e03f21f30a54', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643392/designer-projects/ynjve7gw41cqmmjxyrsm.avif', TRUE, NOW()),
                                                                                ('98f3c942-0684-480c-9ad1-50e6897c3a1b', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'https://res.cloudinary.com/dcyo10lft/image/upload/v1779643392/designer-projects/rmqrnv9rdvpeq9ucywqq.avif', TRUE, NOW())
ON CONFLICT (id) DO NOTHING;

-- 6. Chèn Products_Categories
INSERT INTO public.products_categories (category_id, product_id) VALUES
                                                                     ('9f3159e2-1aca-4d8f-9676-6bf5aa7df6ed', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c'),
                                                                     ('6bbe712f-814a-441c-8fed-be748f440805', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c'),
                                                                     ('a139db66-5879-4352-a1c9-deea8b8eac98', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c')
ON CONFLICT (category_id, product_id) DO NOTHING;

-- Bật lại kiểm tra FK
SET session_replication_role = 'origin';
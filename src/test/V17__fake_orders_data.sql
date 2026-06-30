-- Seed fake orders and order items for testing
-- Designer ID for all: 91717078-9da1-40f9-b1d6-922e3f561e8d

-- 1. Orders for User 702edca5-4eaa-43b5-921c-f3e2136f3a40 (Hoang Phan)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d0', '702edca5-4eaa-43b5-921c-f3e2136f3a40', '91717078-9da1-40f9-b1d6-922e3f561e8d', 850000.00, 'COMPLETED', NOW() - INTERVAL '10 days'),
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d1', '702edca5-4eaa-43b5-921c-f3e2136f3a40', '91717078-9da1-40f9-b1d6-922e3f561e8d', 720000.00, 'NOT_PAID', NOW() - INTERVAL '2 days'),
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d2', '702edca5-4eaa-43b5-921c-f3e2136f3a40', '91717078-9da1-40f9-b1d6-922e3f561e8d', 1920000.00, 'COMPLETED', NOW() - INTERVAL '5 days')
ON CONFLICT (id) DO NOTHING;

-- Order items for User 702edca5-4eaa-43b5-921c-f3e2136f3a40
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a0', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d0', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a1', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d1', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a2', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d2', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a3', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d2', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a0', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d0', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a1', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d1', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a2', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d2', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a3', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d2', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 2. Orders for User beaac47c-fead-4c28-a881-04201eabb282 (Phan Hoang)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d3', 'beaac47c-fead-4c28-a881-04201eabb282', '91717078-9da1-40f9-b1d6-922e3f561e8d', 550000.00, 'COMPLETED', NOW() - INTERVAL '15 days'),
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d4', 'beaac47c-fead-4c28-a881-04201eabb282', '91717078-9da1-40f9-b1d6-922e3f561e8d', 1495000.00, 'NOT_PAID', NOW() - INTERVAL '1 day')
ON CONFLICT (id) DO NOTHING;

-- Order items for User beaac47c-fead-4c28-a881-04201eabb282
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a4', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d3', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a5', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d4', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a6', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d4', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a4', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d3', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a5', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d4', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a6', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d4', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 3. Orders for User 1346b191-ca81-4b0b-a7d1-166a558bc802 (Phan Hoang)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d5', '1346b191-ca81-4b0b-a7d1-166a558bc802', '91717078-9da1-40f9-b1d6-922e3f561e8d', 645000.00, 'COMPLETED', NOW() - INTERVAL '8 days'),
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d6', '1346b191-ca81-4b0b-a7d1-166a558bc802', '91717078-9da1-40f9-b1d6-922e3f561e8d', 1200000.00, 'NOT_PAID', NOW() - INTERVAL '3 days')
ON CONFLICT (id) DO NOTHING;

-- Order items for User 1346b191-ca81-4b0b-a7d1-166a558bc802
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a7', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d5', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a8', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d6', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a7', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d5', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a8', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d6', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 4. Orders for User 22fe8881-f16a-4921-ab7c-ee12ace5851b (Hoang Phan)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d7', '22fe8881-f16a-4921-ab7c-ee12ace5851b', '91717078-9da1-40f9-b1d6-922e3f561e8d', 720000.00, 'COMPLETED', NOW() - INTERVAL '12 days')
ON CONFLICT (id) DO NOTHING;

-- Order items for User 22fe8881-f16a-4921-ab7c-ee12ace5851b
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a9', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d7', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1a9', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d7', 'a1fab818-cc4c-434e-be79-1acb85c37f8f', 'Minimalist Web Hero Banner Design for Tech Store', 720000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 5. Orders for User 83a68cc9-c6ee-42fe-b44c-b8191faecb3d (Hoàng Phan Văn / 22130088@st.hcmuaf.edu.vn)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d8', '83a68cc9-c6ee-42fe-b44c-b8191faecb3d', '91717078-9da1-40f9-b1d6-922e3f561e8d', 1750000.00, 'COMPLETED', NOW() - INTERVAL '6 days'),
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d9', '83a68cc9-c6ee-42fe-b44c-b8191faecb3d', '91717078-9da1-40f9-b1d6-922e3f561e8d', 850000.00, 'NOT_PAID', NOW() - INTERVAL '12 hours')
ON CONFLICT (id) DO NOTHING;

-- Order items for User 83a68cc9-c6ee-42fe-b44c-b8191faecb3d
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b0', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d8', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b1', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d8', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b2', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d9', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b0', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d8', 'a22e27d4-9824-4416-ba55-13db340c248c', 'Vintage Photo Restoration & Cinematic Grading System', 1200000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b1', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d8', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1),
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b2', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1d9', 'cb0ec6c0-85e2-4cb4-8b20-8ddc90824c6c', 'Professional E-Commerce Product Retouching & Color Correction Pack', 850000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 6. Orders for User 278a1333-de03-4820-ada7-ac1856fec2c6 (Test User)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1da', '278a1333-de03-4820-ada7-ac1856fec2c6', '91717078-9da1-40f9-b1d6-922e3f561e8d', 550000.00, 'COMPLETED', NOW() - INTERVAL '20 days')
ON CONFLICT (id) DO NOTHING;

-- Order items for User 278a1333-de03-4820-ada7-ac1856fec2c6
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b3', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1da', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b3', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1da', '810463cc-bbb4-4eaf-9022-83d564e07538', 'High-Converting Social Media Banner Kit for Fashion', 550000.00, 1)
ON CONFLICT (id) DO NOTHING;


-- 7. Orders for User d1053d6b-b633-48f1-8aa3-4905edbb0eeb (Test User 2)
INSERT INTO orders (id, customer_id, designer_id, total_price, status, created_at)
VALUES 
('f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1db', 'd1053d6b-b633-48f1-8aa3-4905edbb0eeb', '91717078-9da1-40f9-b1d6-922e3f561e8d', 645000.00, 'COMPLETED', NOW() - INTERVAL '4 days')
ON CONFLICT (id) DO NOTHING;

-- Order items for User d1053d6b-b633-48f1-8aa3-4905edbb0eeb
INSERT INTO order_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b4', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1db', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO order_product_items (id, order_id, product_id, product_title, price_at_purchase, quantity)
VALUES 
('e1a1e4db-3eb5-4089-a9a3-5c8fe22cb1b4', 'f4c1e4db-3eb5-4089-a9a3-5c8fe22cb1db', 'aadcbfd9-56b6-4bb0-b879-f59968d164e5', 'Abstract Neon Event Banner & Typography Template', 645000.00, 1)
ON CONFLICT (id) DO NOTHING;

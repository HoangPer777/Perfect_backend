-- Create tasks table if it doesn't exist to prevent Flyway from failing before Hibernate ddl-auto runs
CREATE TABLE IF NOT EXISTS tasks (
    id             UUID PRIMARY KEY,
    customer_id    UUID REFERENCES users(id),
    designer_id    UUID REFERENCES users(id),
    package_id     UUID,
    title          VARCHAR(255),
    brief_text     TEXT,
    status         VARCHAR(50),
    revisions_left INT,
    actual_price   NUMERIC(19, 2),
    started_at     TIMESTAMP,
    completed_at   TIMESTAMP,
    created_at     TIMESTAMP
);

-- Seed fake tasks data for testing purchase history (services)
-- main test customer: 83a68cc9-c6ee-42fe-b44c-b8191faecb3d
-- main designer: 91717078-9da1-40f9-b1d6-922e3f561e8d

INSERT INTO tasks (id, customer_id, designer_id, package_id, title, brief_text, status, revisions_left, actual_price, started_at, completed_at, created_at)
VALUES
('b1c1e4db-3eb5-4089-a9a3-5c8fe22cb1f0', '83a68cc9-c6ee-42fe-b44c-b8191faecb3d', '91717078-9da1-40f9-b1d6-922e3f561e8d', NULL, 'Thiết kế Logo thương hiệu thời trang nam tối giản', 'Thiết kế logo hình học, sử dụng tone màu đen/trắng thanh lịch.', 'PROCESSING', 3, 1500000.00, NOW() - INTERVAL '3 days', NULL, NOW() - INTERVAL '4 days'),
('b1c1e4db-3eb5-4089-a9a3-5c8fe22cb1f1', '83a68cc9-c6ee-42fe-b44c-b8191faecb3d', '91717078-9da1-40f9-b1d6-922e3f561e8d', NULL, 'Gói Banner Quảng cáo Facebook cho Chiến dịch Mùa hè', 'Bộ 5 banner kích thước chuẩn chạy ads Facebook.', 'COMPLETED', 0, 850000.00, NOW() - INTERVAL '10 days', NOW() - INTERVAL '5 days', NOW() - INTERVAL '12 days'),
('b1c1e4db-3eb5-4089-a9a3-5c8fe22cb1f2', '83a68cc9-c6ee-42fe-b44c-b8191faecb3d', '91717078-9da1-40f9-b1d6-922e3f561e8d', NULL, 'Dựng Video Short/Reels giới thiệu sản phẩm công nghệ', 'Edit video 60s có phụ đề hiệu ứng và nhạc nền sôi động.', 'REVIEWING', 1, 1200000.00, NOW() - INTERVAL '2 days', NULL, NOW() - INTERVAL '2 days')
ON CONFLICT (id) DO NOTHING;

INSERT INTO tasks (id, customer_id, designer_id, package_id, title, brief_text, status, revisions_left, actual_price, started_at, completed_at, created_at)
VALUES
('b1c1e4db-3eb5-4089-a9a3-5c8fe22cb1f3', '702edca5-4eaa-43b5-921c-f3e2136f3a40', '91717078-9da1-40f9-b1d6-922e3f561e8d', NULL, 'Phục chế ảnh chân dung cũ đen trắng', 'Phục hồi màu sắc và làm nét chi tiết khuôn mặt.', 'COMPLETED', 2, 500000.00, NOW() - INTERVAL '8 days', NOW() - INTERVAL '6 days', NOW() - INTERVAL '8 days'),
('b1c1e4db-3eb5-4089-a9a3-5c8fe22cb1f4', 'beaac47c-fead-4c28-a881-04201eabb282', '91717078-9da1-40f9-b1d6-922e3f561e8d', NULL, 'Thiết kế bao bì hộp đựng mỹ phẩm Organic', 'Phong cách tối giản, thân thiện môi trường.', 'PROCESSING', 4, 3500000.00, NOW() - INTERVAL '1 days', NULL, NOW() - INTERVAL '1 days')
ON CONFLICT (id) DO NOTHING;

CREATE TABLE IF NOT EXISTS email_verification_tokens (
    id         BIGSERIAL PRIMARY KEY,
    user_id    UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token      VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMPTZ  NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- Thêm các trường địa chỉ & cấu hình vào bảng users
ALTER TABLE users ADD COLUMN IF NOT EXISTS city VARCHAR(255);
ALTER TABLE users ADD COLUMN IF NOT EXISTS detailed_address VARCHAR(500);
ALTER TABLE users ADD COLUMN IF NOT EXISTS email_notifications BOOLEAN DEFAULT TRUE;
ALTER TABLE users ADD COLUMN IF NOT EXISTS promotional_offers BOOLEAN DEFAULT FALSE;

-- Kích hoạt extension để hỗ trợ UUID (nếu dùng PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Bảng Sessions (Lưu phiên thanh toán)
CREATE TABLE payment_sessions (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  order_id UUID NOT NULL UNIQUE,
                                  amount NUMERIC(19, 2) NOT NULL,
                                  currency VARCHAR(10) NOT NULL,
                                  status VARCHAR(20) NOT NULL,
                                  idempotency_key VARCHAR(100) NOT NULL UNIQUE,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Transactions (Lưu chi tiết giao dịch)
CREATE TABLE payment_transactions (
                                      id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                      session_id UUID NOT NULL REFERENCES payment_sessions(id) ON DELETE CASCADE,
                                      provider VARCHAR(20) NOT NULL,
                                      type VARCHAR(20) NOT NULL,
                                      status VARCHAR(20) NOT NULL,
                                      gateway_txn_id VARCHAR(255),
                                      failure_reason TEXT,
                                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      updated_at TIMESTAMP
);

-- 3. Bảng Stored Payment Methods (Lưu token thanh toán)
CREATE TABLE stored_payment_methods (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                        user_id UUID NOT NULL,
                                        provider VARCHAR(30) NOT NULL,
                                        external_token VARCHAR(255) NOT NULL,
                                        is_default BOOLEAN NOT NULL DEFAULT FALSE,
                                        status VARCHAR(20) NOT NULL,
                                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        CONSTRAINT uk_provider_external_token UNIQUE (provider, external_token)
);

-- Tạo Index để tối ưu truy vấn
CREATE INDEX idx_session_id ON payment_transactions(session_id);
CREATE INDEX idx_txn_reference ON payment_transactions(gateway_txn_id);
CREATE INDEX idx_user_methods ON stored_payment_methods(user_id);
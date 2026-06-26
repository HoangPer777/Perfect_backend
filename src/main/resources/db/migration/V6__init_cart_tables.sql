-- 1. Bảng Carts (Mỗi User chỉ có duy nhất 1 giỏ hàng)
CREATE TABLE IF NOT EXISTS carts (
                                     id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                     user_id     UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                                     created_at  TIMESTAMPTZ DEFAULT NOW(),
                                     updated_at  TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Bảng Cart_Items (Chi tiết các sản phẩm trong giỏ)
CREATE TABLE IF NOT EXISTS cart_items (
                                          id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                          cart_id     UUID NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
                                          product_id  UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
                                          quantity    INT  NOT NULL CHECK (quantity > 0),
                                          created_at  TIMESTAMPTZ DEFAULT NOW(),
                                          updated_at  TIMESTAMPTZ DEFAULT NOW(),

    -- Đảm bảo không trùng lặp sản phẩm trong cùng 1 giỏ
                                          CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id)
);

-- 3. Tạo Index để tăng tốc độ truy vấn
-- Index để tìm giỏ hàng nhanh theo user_id
CREATE INDEX IF NOT EXISTS idx_carts_user_id ON carts(user_id);

-- Index để tìm các sản phẩm trong giỏ hàng nhanh theo cart_id
CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id ON cart_items(cart_id);

-- 4. Trigger tự động cập nhật updated_at (Tùy chọn, để đồng bộ với các bảng khác)
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_carts_updated_at
    BEFORE UPDATE ON carts
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cart_items_updated_at
    BEFORE UPDATE ON cart_items
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
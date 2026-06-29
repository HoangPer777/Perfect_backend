-- 1. Bảng Orders (Cha)
CREATE TABLE IF NOT EXISTS orders (
                                      id UUID PRIMARY KEY,
                                      customer_id UUID NOT NULL REFERENCES users(id),
                                      designer_id UUID NOT NULL REFERENCES users(id),
                                      total_price NUMERIC(19, 2) NOT NULL,
                                      status VARCHAR(20) NOT NULL DEFAULT 'NOT_PAID',
                                      created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. Bảng Order_Items (Con)
CREATE TABLE IF NOT EXISTS order_product_items (
                                                   id UUID PRIMARY KEY,
                                                   order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                                                   product_id UUID NOT NULL REFERENCES products(id),
                                                   product_title VARCHAR(255) NOT NULL,
                                                   price_at_purchase NUMERIC(19, 2) NOT NULL,
                                                   quantity INT NOT NULL
);
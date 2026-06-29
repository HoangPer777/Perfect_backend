-- 1. Bảng Categories
CREATE TABLE IF NOT EXISTS categories (
                                          id UUID PRIMARY KEY,
                                          parent_id UUID REFERENCES categories(id),
                                          icon VARCHAR(50),
                                          name VARCHAR(255) NOT NULL,
                                          slug VARCHAR(255) NOT NULL UNIQUE
);

-- 2. Bảng Products
CREATE TABLE IF NOT EXISTS products (
                                        id UUID PRIMARY KEY,
                                        designer_id UUID REFERENCES users(id),
                                        title VARCHAR(255) NOT NULL,
                                        description TEXT,
                                        price NUMERIC(19, 2), -- Đảm bảo không bị NULL ở code backend
                                        status VARCHAR(20) DEFAULT 'DRAFT',
                                        is_active BOOLEAN DEFAULT TRUE,
                                        thumbnail_url VARCHAR(500),
                                        view_count INT DEFAULT 0,
                                        sold_count INT DEFAULT 0,
                                        rating_avg NUMERIC(3, 2) DEFAULT 0,
                                        created_at TIMESTAMPTZ DEFAULT NOW(),
                                        updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. Bảng trung gian Products_Categories
CREATE TABLE IF NOT EXISTS products_categories (
                                                   product_id UUID REFERENCES products(id) ON DELETE CASCADE,
                                                   category_id UUID REFERENCES categories(id) ON DELETE CASCADE,
                                                   PRIMARY KEY (product_id, category_id)
);

-- 4. Bảng Product_Images
CREATE TABLE IF NOT EXISTS product_images (
                                              id UUID PRIMARY KEY,
                                              product_id UUID REFERENCES products(id) ON DELETE CASCADE,
                                              url VARCHAR(500) NOT NULL,
                                              status BOOLEAN DEFAULT TRUE,
                                              created_at TIMESTAMPTZ DEFAULT NOW()
);
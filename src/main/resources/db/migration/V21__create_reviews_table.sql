-- Create reviews table
CREATE TABLE IF NOT EXISTS reviews (
    id          UUID PRIMARY KEY,
    reviewer_id UUID REFERENCES users(id) ON DELETE SET NULL,
    product_id  UUID REFERENCES products(id) ON DELETE SET NULL,
    task_id     UUID REFERENCES tasks(id) ON DELETE SET NULL,
    rating      INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    content     TEXT,
    reason      VARCHAR(255)
);

-- Index for performance when querying reviews by product
CREATE INDEX IF NOT EXISTS idx_reviews_product_id ON reviews(product_id);

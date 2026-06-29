CREATE TABLE IF NOT EXISTS service_packages (
                                                id UUID PRIMARY KEY,
                                                name VARCHAR(255) NOT NULL,
                                                package_type VARCHAR(50) NOT NULL,
                                                price NUMERIC(19, 2) NOT NULL
);
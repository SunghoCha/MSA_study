CREATE TABLE IF NOT EXISTS catalog (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       product_id VARCHAR(120) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    stock INTEGER NOT NULL,
    unit_price INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO catalog(product_id, product_name, stock, unit_price) VALUES('CATALOG-001', 'Berlin', 100, 100);
INSERT INTO catalog(product_id, product_name, stock, unit_price) VALUES('CATALOG-002', 'Tokyo', 200, 2000);
INSERT INTO catalog(product_id, product_name, stock, unit_price) VALUES('CATALOG-003', 'Stockholm', 300, 3000);
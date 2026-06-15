CREATE TABLE users (
    id UUID NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    password VARCHAR(255) NOT NULL,
    alias VARCHAR(255) NOT NULL UNIQUE,
    account_non_expired BOOLEAN NOT NULL,
    account_non_locked BOOLEAN NOT NULL,
    credentials_non_expired BOOLEAN NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE refresh_tokens (
    id UUID NOT NULL PRIMARY KEY,
    user_id UUID NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE products (
    id UUID NOT NULL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    image_url VARCHAR(1000),
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    seller_id UUID NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_product_seller FOREIGN KEY (seller_id) REFERENCES users(id)
);

CREATE TABLE orders (
    id UUID NOT NULL PRIMARY KEY,
    buyer_id UUID NOT NULL,
    seller_id UUID NOT NULL,
    product_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES users(id),
    CONSTRAINT fk_order_seller FOREIGN KEY (seller_id) REFERENCES users(id),
    CONSTRAINT fk_order_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_alias ON users(alias);
CREATE INDEX idx_product_status ON products(status);
CREATE INDEX idx_product_seller ON products(seller_id);
CREATE INDEX idx_order_buyer ON orders(buyer_id);
CREATE INDEX idx_order_seller ON orders(seller_id);

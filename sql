CREATE TABLE url_mapping (
    id SERIAL PRIMARY KEY,
    short_url VARCHAR(30) UNIQUE NOT NULL,
    destination_url TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_at TIMESTAMP
);

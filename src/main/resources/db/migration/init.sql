CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) UNIQUE,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS applications (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(100) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes VARCHAR(500),
    offer_url VARCHAR(255),
    applied_at DATE NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id)
);
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL
);

CREATE TABLE favorite_places (
                                 id BIGSERIAL PRIMARY KEY,
                                 external_place_id VARCHAR(255) NOT NULL,
                                 name VARCHAR(150) NOT NULL,
                                 category VARCHAR(100),
                                 address VARCHAR(255),
                                 latitude DOUBLE PRECISION,
                                 longitude DOUBLE PRECISION,
                                 image_url VARCHAR(500),
                                 saved_at TIMESTAMP NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 CONSTRAINT fk_favorite_places_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES users(id)
                                             ON DELETE CASCADE
);
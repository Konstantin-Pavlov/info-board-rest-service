--init.sql

-- Create users table
CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email    VARCHAR(50) NOT NULL
);

-- Insert initial data into users table
INSERT INTO users (username, email)
VALUES ('user1', 'user1@example.com'),
       ('user2', 'user2@example.com');

-- Create messages table
CREATE TABLE IF NOT EXISTS messages
(
    id          SERIAL PRIMARY KEY,
    author_id   BIGINT       NOT NULL,
    content     TEXT         NOT NULL,
    author_name VARCHAR(100) NOT NULL,
    timestamp   TIMESTAMP    NOT NULL,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

-- Insert initial data into messages table
INSERT INTO messages (author_id, content, author_name, timestamp)
VALUES (1, 'Hello, this is user1!', 'user1', NOW()),
       (2, 'Hi there, this is user2!', 'user2', NOW());
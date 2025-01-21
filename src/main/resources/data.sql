CREATE TABLE MEMBER (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255) NULL
);

INSERT INTO MEMBER (email, is_verified, verification_token)
VALUES
    ('minjeesong95@gmail.com', false, '');

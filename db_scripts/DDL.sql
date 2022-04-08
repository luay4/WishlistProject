CREATE TABLE users (
    user_id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    "password VARCHAR(50) NOT NULL,
    "PRIMARY KEY (user_id));

CREATE TABLE wishes (user_id INT NOT NULL,
    item_sequence INT NOT NULL,
    item_name VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id));
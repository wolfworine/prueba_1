CREATE DATABASE IF NOT EXISTS UserDB;
USE UserDB;

CREATE TABLE tb_user (
                         id VARCHAR(50) PRIMARY KEY,
                         name VARCHAR(100),
                         email VARCHAR(100),
                         password VARCHAR(255),
                         created TIMESTAMP,
                         modified TIMESTAMP,
                         last_login TIMESTAMP,
                         token VARCHAR(255),
                         is_active BOOLEAN,
                         role VARCHAR(50)
);

CREATE TABLE tb_phone (
                          id VARCHAR(50) PRIMARY KEY,
                          user_id VARCHAR(50),
                          number VARCHAR(20),
                          city_code VARCHAR(10),
                          country_code VARCHAR(10),

                          CONSTRAINT fk_phones_users
                              FOREIGN KEY (user_id)
                                  REFERENCES tb_user(id)
                                  ON DELETE CASCADE
);
DROP TABLE IF EXISTS users;

CREATE TABLE mooc_users (
    username varchar(50) NOT NULL,
    password varchar(100) NOT NULL,
    enabled TINYINT NOT NULL DEFAULT 1,
    name varchar(50) NULL,
    PRIMARY KEY (username)
) ENGINE = INNODB;

DROP TABLE IF EXISTS authorities;
CREATE TABLE mooc_authorities (
     username varchar(50) NOT NULL,
     authority varchar(50) NOT NULL,
     CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES mooc_users(username)
) ENGINE = INNODB;

CREATE UNIQUE INDEX ix_auth_username
    on mooc_authorities (username, authority)
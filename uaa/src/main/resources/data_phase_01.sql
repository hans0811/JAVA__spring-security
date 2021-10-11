
insert into mooc_users(username, password, enabled) values
('user', '{bcrypt}$2a$10$jhS817qUHgOR4uQSoEBRxO58.rZ1dBCmCTjG8PeuQAX4eISf.zowm',1),
('old_user', '{SHA-1}7ce0359f12857f2a90c7de465f40a95f01cb5da9', 1);

insert into mooc_authorities(username, authority) values
('old_user', 'ROLE_USER'),
('user', 'ROLE_USER'),
('user', 'ROLE_ADMIN');
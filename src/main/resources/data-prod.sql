INSERT INTO authority (name) VALUES ('ROLE_ADMIN');
INSERT INTO authority (name) VALUES ('ROLE_USER');
INSERT INTO authority (name) VALUES ('ROLE_WRITER');

INSERT INTO user (username, password_hash, display_name, email, activated, activation_key, created_date, reset_key, reset_date) VALUES ('admin', '$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC', '系统管理员', 'admin@jblog', true, null, '2021-02-18 00:00:00', null, null);

INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_USER');
INSERT INTO user_authority (user_id, authority_name) VALUES (1, 'ROLE_WRITER');
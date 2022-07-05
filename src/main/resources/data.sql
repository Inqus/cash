INSERT INTO cash_box (id, balance)
VALUES ( 1, 1000000),
       ( 2, 1000000)
ON CONFLICT DO NOTHING;

INSERT INTO user_table (id,username, password, role, cash_box_id , fio)
VALUES ( 1, 'cash1', '$2a$10$IozeYOly85yTr4ysKDUgveDQwLFsbdWTTkqUxrdZI/NgHGnh.nE12','ROLE_USER', 1, 'cash1'),
       ( 2, 'cash2', '$2a$10$IozeYOly85yTr4ysKDUgveDQwLFsbdWTTkqUxrdZI/NgHGnh.nE12','ROLE_USER', 2, 'cash2'),
       ( 3, 'admin', '$2a$10$IozeYOly85yTr4ysKDUgveDQwLFsbdWTTkqUxrdZI/NgHGnh.nE12','ROLE_ADMIN', null, 'admin')
ON CONFLICT DO NOTHING;


INSERT INTO currency (id, name)
VALUES ( 1, 'KGS'),
       ( 2, 'USD')
ON CONFLICT DO NOTHING;

INSERT INTO currency_rate (id, from_currency, to_currency, rate)
VALUES ( 1, 1, 2, 0.013),
       ( 2, 2, 1, 80)
ON CONFLICT DO NOTHING;
INSERT INTO supplier (uuid, name, address) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'ООО Большой Сад', 'г. Волгоград, ул. Садовая 1'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'ЗАО Много фруктов', 'г. Краснодар, ул. Полевая 5'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Фермер Иван', 'г. Ростов, д. Петровка');

INSERT INTO product (id, name, info, measurement_unit) VALUES
(nextval('product_seq'), 'Яблоко Гала', 'Сладкое красное яблоко', 'кг'),
(nextval('product_seq'), 'Яблоко Голден', 'Желтое сочное яблоко', 'кг'),
(nextval('product_seq'), 'Яблоко Смит', 'Зелёное яблоко', 'кг'),
(nextval('product_seq'), 'Яблоко Антоновка', 'Кисло-сладкое яблоко', 'кг'),
(nextval('product_seq'), 'Яблоко Фуджи', 'Хрустящее сладкое яблоко', 'кг'),
(nextval('product_seq'), 'Яблоко Рэд Делишес', 'Красное сладкое яблоко', 'кг'),
(nextval('product_seq'), 'Груша Конференция', 'Твердая зеленая груша', 'кг'),
(nextval('product_seq'), 'Груша Дюшес', 'Мягкая сладкая груша', 'кг'),
(nextval('product_seq'), 'Груша Бере', 'Сладкая летняя груша', 'кг'),
(nextval('product_seq'), 'Груша Аббат', 'Ароматная столовая груша', 'кг');

INSERT INTO product_price_period (id, supplier_id, product_id, start_date, end_date, price_per_measurement_unit) VALUES
(nextval('product_price_period_seq'), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', (SELECT id FROM product WHERE name='Яблоко Гала'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 100.00),
(nextval('product_price_period_seq'), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', (SELECT id FROM product WHERE name='Яблоко Голден'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 110.00),
(nextval('product_price_period_seq'), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', (SELECT id FROM product WHERE name='Груша Конференция'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 150.00),
(nextval('product_price_period_seq'), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', (SELECT id FROM product WHERE name='Груша Дюшес'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 160.00);

INSERT INTO product_price_period (id, supplier_id, product_id, start_date, end_date, price_per_measurement_unit) VALUES
(nextval('product_price_period_seq'), 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', (SELECT id FROM product WHERE name='Яблоко Смит'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 90.00),
(nextval('product_price_period_seq'), 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', (SELECT id FROM product WHERE name='Яблоко Антоновка'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 95.00),
(nextval('product_price_period_seq'), 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', (SELECT id FROM product WHERE name='Груша Конференция'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 140.00),
(nextval('product_price_period_seq'), 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', (SELECT id FROM product WHERE name='Груша Бере'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 130.00);

INSERT INTO product_price_period (id, supplier_id, product_id, start_date, end_date, price_per_measurement_unit) VALUES
(nextval('product_price_period_seq'), 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', (SELECT id FROM product WHERE name='Яблоко Фуджи'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 120.00),
(nextval('product_price_period_seq'), 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', (SELECT id FROM product WHERE name='Яблоко Рэд Делишес'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 125.00),
(nextval('product_price_period_seq'), 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', (SELECT id FROM product WHERE name='Груша Дюшес'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 170.00),
(nextval('product_price_period_seq'), 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', (SELECT id FROM product WHERE name='Груша Аббат'), '2024-01-01 00:00:00', '2030-12-31 23:59:59', 135.00);

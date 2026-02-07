CREATE TABLE supplier (
    uuid UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE SEQUENCE product_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE product (
    id BIGINT NOT NULL DEFAULT nextval('product_seq') PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    info VARCHAR(255),
    measurement_unit VARCHAR(255) NOT NULL
);

CREATE SEQUENCE product_price_period_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE product_price_period (
    id BIGINT NOT NULL DEFAULT nextval('product_price_period_seq') PRIMARY KEY,
    supplier_id UUID NOT NULL REFERENCES supplier(uuid),
    product_id BIGINT NOT NULL REFERENCES product(id),
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    price_per_measurement_unit NUMERIC(19, 2) NOT NULL
);

CREATE SEQUENCE delivery_record_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE delivery_record (
    id BIGINT NOT NULL DEFAULT nextval('delivery_record_seq') PRIMARY KEY,
    supplier_id UUID NOT NULL REFERENCES supplier(uuid),
    date TIMESTAMP NOT NULL,
    info VARCHAR(255)
);

CREATE SEQUENCE delivery_item_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE delivery_item (
    id BIGINT NOT NULL DEFAULT nextval('delivery_item_seq') PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES product(id),
    delivery_record_id BIGINT NOT NULL REFERENCES delivery_record(id),
    quantity NUMERIC(19, 2) NOT NULL,
    acceptance BOOLEAN NOT NULL,
    price_of_accepted NUMERIC(19, 2)
);
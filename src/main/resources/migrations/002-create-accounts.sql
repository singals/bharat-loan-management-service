CREATE TABLE IF NOT EXISTS accounts(
    id UUID PRIMARY KEY,
    number BIGSERIAL,
    customer_id UUID REFERENCES customers(id)
);
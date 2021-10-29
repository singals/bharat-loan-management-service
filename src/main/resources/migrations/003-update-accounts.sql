ALTER TABLE accounts RENAME TO loan_accounts;
ALTER TABLE loan_accounts
    ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ADD COLUMN initial_amount BIGINT,
    ADD COLUMN initial_duration_months INTEGER;
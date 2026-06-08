-- Unified transactions table replacing `transaction` and `personal_expenses`

-- 1. Create unified table
CREATE TABLE transactions (
    id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id             UUID            NOT NULL,
    context             VARCHAR(16)     NOT NULL,

    date                DATE            NOT NULL,
    amount              NUMERIC(19,4)   NOT NULL,
    currency            VARCHAR(3)      NOT NULL DEFAULT 'EUR',
    type                VARCHAR(8)      NOT NULL DEFAULT 'DEBIT',

    category_id         UUID            REFERENCES categories(id),

    counterparty        VARCHAR(255),
    external_reference  VARCHAR(255),
    description         TEXT,
    notes               TEXT,

    source              VARCHAR(16)     NOT NULL DEFAULT 'MANUAL',

    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    version             INTEGER         NOT NULL DEFAULT 0,
    deleted_at          TIMESTAMPTZ,

    CONSTRAINT chk_context  CHECK (context IN ('PERSONAL', 'BUSINESS')),
    CONSTRAINT chk_type     CHECK (type IN ('DEBIT', 'CREDIT')),
    CONSTRAINT chk_source   CHECK (source IN ('MANUAL', 'IMPORT', 'BANK_SYNC'))
);

CREATE INDEX idx_transactions_user_context_date
    ON transactions(user_id, context, date DESC);

-- 2. Migrate existing business transactions
-- Replace <BUSINESS_USER_UUID> with the actual user sub from identity-service
INSERT INTO transactions (
    user_id, context, date, amount, currency, type,
    category_id, counterparty, external_reference, description, source, created_at
)
SELECT
    '00000000-0000-0000-0000-000000000001'::UUID,
    'BUSINESS',
    date,
    amount,
    'EUR',
    CASE WHEN amount < 0 THEN 'DEBIT' ELSE 'CREDIT' END,
    category_id,
    counterparty,
    reference,
    description,
    'IMPORT',
    NOW()
FROM transaction;

-- 3. Migrate personal_expenses
-- Replace <PERSONAL_USER_UUID> with the actual user sub from identity-service
INSERT INTO transactions (
    user_id, context, date, amount, currency, type,
    description, source, created_at
)
SELECT
    '00000000-0000-0000-0000-000000000001'::UUID,
    'PERSONAL',
    date,
    amount,
    'EUR',
    CASE WHEN amount < 0 THEN 'DEBIT' ELSE 'CREDIT' END,
    description,
    'IMPORT',
    created_at
FROM personal_expenses;

-- 4. Drop old tables
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS personal_expenses;

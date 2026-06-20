-- Fixed expenses: recurring monthly commitments (rent, subscriptions, insurance, loans…).
-- Mirrors the transactions table conventions (UUID pk, NUMERIC(19,4) amount, TIMESTAMPTZ audit
-- columns, optimistic-lock version) but is hard-deleted, so there is no deleted_at column.

CREATE TABLE fixed_expenses (
    id           UUID          PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id      UUID          NOT NULL,
    context      VARCHAR(16)   NOT NULL DEFAULT 'PERSONAL',

    name         VARCHAR(255)  NOT NULL,
    amount       NUMERIC(19,4) NOT NULL,
    currency     VARCHAR(3)    NOT NULL DEFAULT 'EUR',

    category_id  UUID          REFERENCES categories(id),

    billing_day  INTEGER       NOT NULL,
    active       BOOLEAN       NOT NULL DEFAULT TRUE,
    notes        TEXT,

    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    version      INTEGER       NOT NULL DEFAULT 0,

    CONSTRAINT chk_fx_context     CHECK (context IN ('PERSONAL', 'BUSINESS')),
    CONSTRAINT chk_fx_billing_day CHECK (billing_day BETWEEN 1 AND 31)
);

CREATE INDEX idx_fixed_expenses_user_context
    ON fixed_expenses(user_id, context);

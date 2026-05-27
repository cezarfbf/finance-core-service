CREATE TYPE category_context AS ENUM ('PERSONAL', 'BUSINESS');

CREATE TABLE categories (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID,
    context     category_context NOT NULL,
    code        VARCHAR(64),
    name        VARCHAR(100) NOT NULL,
    icon        VARCHAR(64),
    color       VARCHAR(7),
    sort_order  INTEGER     NOT NULL DEFAULT 0,
    parent_id   UUID        REFERENCES categories(id) ON DELETE RESTRICT,
    archived_at TIMESTAMPTZ,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    version     BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT chk_category_ownership CHECK (
        (user_id IS NULL AND code IS NOT NULL)
        OR
        (user_id IS NOT NULL AND context = 'PERSONAL' AND code IS NULL)
    ),

    -- system categories: unique (context, code) — NULLs excluded by chk_category_ownership
    CONSTRAINT uq_system_category UNIQUE (context, code),

    -- user categories: unique (user_id, context, name) — NULLs excluded by chk_category_ownership
    CONSTRAINT uq_user_category UNIQUE (user_id, context, name)
);

CREATE INDEX idx_categories_context_user_active
    ON categories (context, user_id)
    WHERE archived_at IS NULL;

CREATE INDEX idx_categories_id_active
    ON categories (id)
    WHERE archived_at IS NULL;

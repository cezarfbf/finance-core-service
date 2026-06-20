-- Dev seed: a handful of fixed expenses for the seeded dev user (see V8__unify_transactions.sql),
-- linked to the system PERSONAL categories seeded in V9. Category ids are resolved by code so this
-- works regardless of the generated UUIDs.

INSERT INTO fixed_expenses (user_id, context, name, amount, currency, category_id, billing_day, active, notes)
VALUES
    ('00000000-0000-0000-0000-000000000001', 'PERSONAL', 'Renda',   750.00, 'EUR',
        (SELECT id FROM categories WHERE user_id IS NULL AND context = 'PERSONAL' AND code = 'rent'),          8, TRUE, NULL),
    ('00000000-0000-0000-0000-000000000001', 'PERSONAL', 'Netflix',  13.99, 'EUR',
        (SELECT id FROM categories WHERE user_id IS NULL AND context = 'PERSONAL' AND code = 'subscriptions'), 2, TRUE, NULL),
    ('00000000-0000-0000-0000-000000000001', 'PERSONAL', 'Ginásio',  29.90, 'EUR',
        (SELECT id FROM categories WHERE user_id IS NULL AND context = 'PERSONAL' AND code = 'subscriptions'), 5, TRUE, NULL),
    ('00000000-0000-0000-0000-000000000001', 'PERSONAL', 'Seguro auto', 45.00, 'EUR',
        (SELECT id FROM categories WHERE user_id IS NULL AND context = 'PERSONAL' AND code = 'insurance'),    15, TRUE, NULL),
    ('00000000-0000-0000-0000-000000000001', 'PERSONAL', 'Internet + TV', 39.99, 'EUR',
        (SELECT id FROM categories WHERE user_id IS NULL AND context = 'PERSONAL' AND code = 'utilities'),    20, TRUE, NULL);

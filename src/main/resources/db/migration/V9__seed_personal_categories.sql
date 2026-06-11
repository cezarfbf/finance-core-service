-- System PERSONAL categories (user_id NULL, code required by chk_category_ownership;
-- unique by (context, code) via uq_system_category). Mirrors V6 business seed.
INSERT INTO categories (user_id, context, code, name, sort_order, created_at, updated_at, version)
VALUES
    (NULL, 'PERSONAL', 'groceries',     'Groceries',     0,  now(), now(), 0),
    (NULL, 'PERSONAL', 'rent',          'Rent',          1,  now(), now(), 0),
    (NULL, 'PERSONAL', 'utilities',     'Utilities',     2,  now(), now(), 0),
    (NULL, 'PERSONAL', 'transport',     'Transport',     3,  now(), now(), 0),
    (NULL, 'PERSONAL', 'health',        'Health',        4,  now(), now(), 0),
    (NULL, 'PERSONAL', 'dining',        'Dining',        5,  now(), now(), 0),
    (NULL, 'PERSONAL', 'salary',        'Salary',        6,  now(), now(), 0),
    (NULL, 'PERSONAL', 'subscriptions', 'Subscriptions', 7,  now(), now(), 0),
    (NULL, 'PERSONAL', 'travel',        'Travel',        8,  now(), now(), 0),
    (NULL, 'PERSONAL', 'insurance',     'Insurance',     9,  now(), now(), 0),
    (NULL, 'PERSONAL', 'shopping',      'Shopping',      10, now(), now(), 0),
    (NULL, 'PERSONAL', 'others',        'Others',        11, now(), now(), 0);

ALTER TABLE transaction
    ADD COLUMN category_id UUID REFERENCES categories(id) ON DELETE SET NULL;

UPDATE transaction t
SET category_id = (
    SELECT c.id
    FROM categories c
    WHERE c.code = t.category
      AND c.context = 'BUSINESS'
)
WHERE t.category IS NOT NULL
  AND t.category != '-';

CREATE INDEX idx_transaction_category_id ON transaction (category_id);

ALTER TABLE transaction DROP COLUMN category;

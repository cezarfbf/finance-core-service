CREATE TABLE personal_expenses (
  id          BIGSERIAL     PRIMARY KEY,
  date        DATE,
  category    VARCHAR(255),
  type        VARCHAR(50),
  amount      NUMERIC(15,2),
  description VARCHAR(255),
  created_at  TIMESTAMP     NOT NULL
);

CREATE INDEX idx_personal_expenses_date ON personal_expenses(date);

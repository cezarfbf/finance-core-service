CREATE TABLE transaction (
  id             BIGSERIAL PRIMARY KEY,
  date           DATE           NOT NULL,
  counterparty   VARCHAR(255)   NOT NULL,
  reference      VARCHAR(255),
  category       VARCHAR(100),
  amount         NUMERIC(15,2)  NOT NULL,
  classification VARCHAR(50)    NOT NULL,
  description    TEXT
);

CREATE INDEX idx_transaction_date ON transaction(date);

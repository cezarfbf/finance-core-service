-- Seed data for personal expenses (DELETE first so re-runs stay idempotent)
DELETE FROM personal_expenses;

INSERT INTO personal_expenses (date, category, type, amount, description, created_at) VALUES
('2025-12-01', 'house_rental', 'FIXED', 1000.00, 'Monthly rent', NOW()),
('2025-12-02', 'internet', 'FIXED', 50.00, 'Internet bill', NOW()),
('2025-12-03', 'insurance', 'FIXED', 200.00, 'Health insurance', NOW()),
('2025-12-05', 'church', 'FIXED', 100.00, 'Church donation', NOW()),
('2025-12-10', 'loan', 'FIXED', 1150.00, 'Personal loan payment', NOW()),
('2025-12-05', 'gym', 'VARIABLE', 50.00, 'Gym membership', NOW()),
('2025-12-12', 'sports', 'VARIABLE', 75.00, 'Tennis lesson', NOW()),
('2025-12-15', 'sports', 'VARIABLE', 125.00, 'Hiking equipment', NOW()),
('2025-12-08', 'care', 'VARIABLE', 80.00, 'Medical consultation', NOW()),
('2025-12-20', 'care', 'VARIABLE', 20.00, 'Pharmacy', NOW());

INSERT INTO personal_expenses (date, category, type, amount, description, created_at) VALUES
('2025-11-01', 'house_rental', 'FIXED', 1000.00, 'Monthly rent', NOW()),
('2025-11-02', 'internet', 'FIXED', 50.00, 'Internet bill', NOW()),
('2025-11-03', 'insurance', 'FIXED', 200.00, 'Health insurance', NOW()),
('2025-11-05', 'church', 'FIXED', 100.00, 'Church donation', NOW()),
('2025-11-10', 'loan', 'FIXED', 1150.00, 'Personal loan payment', NOW()),
('2025-11-08', 'gym', 'VARIABLE', 50.00, 'Gym membership', NOW()),
('2025-11-15', 'sports', 'VARIABLE', 100.00, 'Football match', NOW()),
('2025-11-20', 'care', 'VARIABLE', 60.00, 'Dentist appointment', NOW());

INSERT INTO personal_expenses (date, category, type, amount, description, created_at) VALUES
('2025-10-01', 'house_rental', 'FIXED', 1000.00, 'Monthly rent', NOW()),
('2025-10-02', 'internet', 'FIXED', 50.00, 'Internet bill', NOW()),
('2025-10-03', 'insurance', 'FIXED', 200.00, 'Health insurance', NOW()),
('2025-10-05', 'church', 'FIXED', 100.00, 'Church donation', NOW()),
('2025-10-10', 'loan', 'FIXED', 1150.00, 'Personal loan payment', NOW()),
('2025-10-12', 'gym', 'VARIABLE', 50.00, 'Gym membership', NOW()),
('2025-10-18', 'sports', 'VARIABLE', 150.00, 'Marathon registration', NOW()),
('2025-10-25', 'care', 'VARIABLE', 45.00, 'Eye exam', NOW());

INSERT INTO personal_expenses (date, category, type, amount, description, created_at) VALUES
('2026-05-01', 'house_rental', 'FIXED', 1000.00, 'Monthly rent', NOW()),
('2026-05-02', 'internet', 'FIXED', 50.00, 'Internet bill', NOW()),
('2026-05-03', 'insurance', 'FIXED', 200.00, 'Health insurance', NOW()),
('2026-05-05', 'church', 'FIXED', 100.00, 'Church donation', NOW()),
('2026-05-10', 'loan', 'FIXED', 1150.00, 'Personal loan payment', NOW()),
('2026-05-02', 'gym', 'VARIABLE', 50.00, 'Gym membership', NOW()),
('2026-05-08', 'care', 'VARIABLE', 35.00, 'Pharmacy', NOW());

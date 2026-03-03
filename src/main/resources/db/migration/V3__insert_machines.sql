-- =====================================================
-- Flyway Migration: Insert sample machines into TBL_MACHINE
-- =====================================================

INSERT INTO TBL_MACHINE (NAME, TYPE, IS_ACTIVE, CREATED_BY, CREATED_DATE)
VALUES
('CNC-Frame-01', 'CNC', TRUE, 1, CURRENT_TIMESTAMP),
('CNC-Frame-02', 'CNC', TRUE, 1, CURRENT_TIMESTAMP),
('Paint-Booth-01', 'Paint', TRUE, 1, CURRENT_TIMESTAMP),
('Assembly-Line-01', 'Assembly', TRUE, 1, CURRENT_TIMESTAMP),
('Quality-Check-01', 'QC', TRUE, 1, CURRENT_TIMESTAMP);
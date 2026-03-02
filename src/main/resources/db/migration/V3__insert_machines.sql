-- =====================================================
-- Flyway Migration: Insert sample machines into TBL_MACHINE
-- =====================================================

INSERT INTO TBL_MACHINE (NAME, TYPE, STATUS, IS_ACTIVE, CREATED_BY, CREATED_DATE)
VALUES
('CNC-Frame-01', 'CNC', 'IDLE', TRUE, 1, CURRENT_TIMESTAMP),
('CNC-Frame-02', 'CNC', 'IDLE', TRUE, 1, CURRENT_TIMESTAMP),
('Paint-Booth-01', 'Paint', 'IDLE', TRUE, 1, CURRENT_TIMESTAMP),
('Assembly-Line-01', 'Assembly', 'IDLE', TRUE, 1, CURRENT_TIMESTAMP),
('Quality-Check-01', 'QC', 'IDLE', TRUE, 1, CURRENT_TIMESTAMP);
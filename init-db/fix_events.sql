-- =================================================================
-- SCRIPT URGENȚĂ POPULARE EVENIMENTE
-- =================================================================

-- 1. Insert Participanți la Evenimente (linkat cu Projects)
-- Project 2 (Curatenie) - Event 1 is generic for Proj 2
INSERT INTO event_participants (event_id, volunteer_id, attendance_status, registration_date) VALUES
((SELECT id FROM events WHERE project_id=2 LIMIT 1), 1, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=2 LIMIT 1), 2, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=2 LIMIT 1), 4, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=2 LIMIT 1), 6, 'ATTENDED', NOW());

-- Project 7 (Curs Prim Ajutor) - Events 5, 6
INSERT INTO event_participants (event_id, volunteer_id, attendance_status, registration_date) VALUES
((SELECT id FROM events WHERE project_id=7 AND title LIKE '%Teoretica%' LIMIT 1), 1, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=7 AND title LIKE '%Teoretica%' LIMIT 1), 2, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=7 AND title LIKE '%Teoretica%' LIMIT 1), 4, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=7 AND title LIKE '%Practica%' LIMIT 1), 1, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=7 AND title LIKE '%Practica%' LIMIT 1), 2, 'ATTENDED', NOW());

-- Project 14 (Hackathon) - Events 9, 10, 11
INSERT INTO event_participants (event_id, volunteer_id, attendance_status, registration_date) VALUES
((SELECT id FROM events WHERE project_id=14 AND title LIKE '%Day 1%' LIMIT 1), 3, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=14 AND title LIKE '%Day 1%' LIMIT 1), 15, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=14 AND title LIKE '%Day 1%' LIMIT 1), 1, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=14 AND title LIKE '%Final%' LIMIT 1), 3, 'ATTENDED', NOW());

-- Project 18 (Festival Jazz) - Events 14, 15
INSERT INTO event_participants (event_id, volunteer_id, attendance_status, registration_date) VALUES
((SELECT id FROM events WHERE project_id=18 AND title LIKE '%Concert%' LIMIT 1), 6, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=18 AND title LIKE '%Concert%' LIMIT 1), 8, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=18 AND title LIKE '%Concert%' LIMIT 1), 10, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=18 AND title LIKE '%Concert%' LIMIT 1), 11, 'ATTENDED', NOW()),
((SELECT id FROM events WHERE project_id=18 AND title LIKE '%Concert%' LIMIT 1), 12, 'ATTENDED', NOW());

-- Populate a few scheduled ones as REGISTERED
-- Project 1 (Impadurire) - Scheduled
INSERT INTO event_participants (event_id, volunteer_id, attendance_status, registration_date) VALUES
((SELECT id FROM events WHERE project_id=1 LIMIT 1), 22, 'REGISTERED', NOW()),
((SELECT id FROM events WHERE project_id=1 LIMIT 1), 23, 'REGISTERED', NOW());

-- 2. Update Counters
UPDATE events e SET current_participants = (SELECT COUNT(*) FROM event_participants ep WHERE ep.event_id = e.id);

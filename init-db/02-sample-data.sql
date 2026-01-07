-- ============================================
-- SAMPLE DATA FOR TESTING
-- ============================================

-- Organizații
INSERT INTO organizations (name, description, email, phone, address, registration_number, status) VALUES
                                                                                                      ('Habitat for Humanity România', 'Construim case și speranță împreună', 'contact@habitat.ro', '0212345678', 'Str. Speranței nr. 10, București', 'ORG001', 'ACTIVE'),
                                                                                                      ('Asociația Pentru Valori', 'Educație și dezvoltare comunitară', 'info@pentruvalori.ro', '0213456789', 'Bd. Unirii nr. 45, Cluj-Napoca', 'ORG002', 'ACTIVE'),
                                                                                                      ('Greenpeace România', 'Protejăm mediul pentru generațiile viitoare', 'office@greenpeace.ro', '0214567890', 'Str. Ecologiei nr. 22, Timișoara', 'ORG003', 'ACTIVE'),
                                                                                                      ('Salvați Copiii', 'Fiecare copil merită o copilărie', 'contact@salvaticopiii.ro', '0215678901', 'Calea Victoriei nr. 100, București', 'ORG004', 'ACTIVE');

-- Voluntari
INSERT INTO volunteers (first_name, last_name, email, phone, date_of_birth, cnp, address, city, county, registration_date, status, bio) VALUES
                                                                                                                                            ('Ion', 'Popescu', 'ion.popescu@email.com', '0721234567', '1995-03-15', '1950315123456', 'Str. Libertății nr. 5', 'București', 'București', '2023-01-15', 'ACTIVE', 'Pasionat de educație și dezvoltare comunitară'),
                                                                                                                                            ('Maria', 'Ionescu', 'maria.ionescu@email.com', '0722345678', '1998-07-22', '2980722234567', 'Bd. Independenței nr. 34', 'Cluj-Napoca', 'Cluj', '2023-02-20', 'ACTIVE', 'Iubesc natura și activitățile în aer liber'),
                                                                                                                                            ('Andrei', 'Georgescu', 'andrei.g@email.com', '0723456789', '1992-11-08', '1921108345678', 'Str. Mihai Viteazu nr. 12', 'Timișoara', 'Timiș', '2023-03-10', 'ACTIVE', 'Programator care vrea să ajute comunitatea'),
                                                                                                                                            ('Elena', 'Dumitrescu', 'elena.d@email.com', '0724567890', '2000-05-30', '2000530456789', 'Aleea Rozelor nr. 8', 'Iași', 'Iași', '2023-04-05', 'ACTIVE', 'Student la medicină, pasionată de social'),
                                                                                                                                            ('Cristian', 'Popa', 'cristian.popa@email.com', '0725678901', '1990-09-17', '1900917567890', 'Str. Unirii nr. 56', 'Brașov', 'Brașov', '2023-05-12', 'ACTIVE', 'Instructor de prim ajutor'),
                                                                                                                                            ('Ana', 'Marinescu', 'ana.m@email.com', '0726789012', '1997-12-03', '2971203678901', 'Bd. Carol I nr. 78', 'Constanța', 'Constanța', '2023-06-18', 'ACTIVE', 'Designer grafic și artist'),
                                                                                                                                            ('Mihai', 'Stoica', 'mihai.stoica@email.com', '0727890123', '1994-04-25', '1940425789012', 'Str. Plopilor nr. 15', 'Craiova', 'Dolj', '2023-07-22', 'ACTIVE', 'Fotograf voluntar'),
                                                                                                                                            ('Laura', 'Constantinescu', 'laura.c@email.com', '0728901234', '1999-08-14', '2990814890123', 'Aleea Parcului nr. 3', 'Sibiu', 'Sibiu', '2023-08-30', 'ACTIVE', 'Pasionată de copii și educație');

-- Competențe
INSERT INTO skills (name, description, category) VALUES
                                                     ('Programare Java', 'Dezvoltare aplicații în Java', 'TECHNICAL'),
                                                     ('Limba Engleză', 'Comunicare în limba engleză', 'LANGUAGE'),
                                                     ('Prim Ajutor', 'Acordare prim ajutor', 'SOCIAL'),
                                                     ('Management Proiecte', 'Coordonare și planificare proiecte', 'MANAGEMENT'),
                                                     ('Design Grafic', 'Creare materiale vizuale', 'CREATIVE'),
                                                     ('Construcții', 'Muncă în construcții', 'TECHNICAL'),
                                                     ('Predare', 'Abilități didactice', 'SOCIAL'),
                                                     ('Social Media', 'Gestionare rețele sociale', 'TECHNICAL'),
                                                     ('Fotografie', 'Fotografie evenimente', 'CREATIVE'),
                                                     ('Comunicare', 'Abilități de comunicare', 'SOCIAL'),
                                                     ('Lucru în echipă', 'Colaborare eficientă', 'SOCIAL'),
                                                     ('Limba Franceză', 'Comunicare în limba franceză', 'LANGUAGE');

-- Competențe Voluntari
INSERT INTO volunteer_skills (volunteer_id, skill_id, proficiency_level, years_experience) VALUES
                                                                                               (1, 7, 'ADVANCED', 5),
                                                                                               (1, 10, 'EXPERT', 8),
                                                                                               (2, 2, 'ADVANCED', 6),
                                                                                               (2, 11, 'ADVANCED', 4),
                                                                                               (3, 1, 'EXPERT', 7),
                                                                                               (3, 8, 'INTERMEDIATE', 2),
                                                                                               (4, 3, 'INTERMEDIATE', 1),
                                                                                               (4, 7, 'BEGINNER', 1),
                                                                                               (5, 3, 'EXPERT', 10),
                                                                                               (5, 4, 'ADVANCED', 5),
                                                                                               (6, 5, 'EXPERT', 8),
                                                                                               (6, 8, 'ADVANCED', 4),
                                                                                               (7, 9, 'EXPERT', 6),
                                                                                               (7, 10, 'ADVANCED', 5),
                                                                                               (8, 7, 'INTERMEDIATE', 2),
                                                                                               (8, 11, 'ADVANCED', 3);

-- Proiecte
INSERT INTO projects (organization_id, title, description, start_date, end_date, location, city, county, max_volunteers, status) VALUES
                                                                                                                                     (1, 'Construim Speranță 2024', 'Construcție case pentru familii defavorizate', '2024-03-01', '2024-08-31', 'Str. Construcției nr. 50', 'București', 'București', 20, 'ACTIVE'),
                                                                                                                                     (2, 'After-School pentru Copii', 'Program educațional după ore', '2024-02-01', '2024-06-30', 'Școala Generală nr. 5', 'Cluj-Napoca', 'Cluj', 15, 'ACTIVE'),
                                                                                                                                     (3, 'Curățăm Parcurile', 'Acțiune de ecologizare', '2024-04-15', '2024-04-15', 'Parcul Central', 'Timișoara', 'Timiș', 50, 'ACTIVE'),
                                                                                                                                     (4, 'Vacanță Fericită', 'Tabără pentru copii defavorizați', '2024-07-01', '2024-07-15', 'Cabana Predeal', 'Predeal', 'Brașov', 10, 'DRAFT'),
                                                                                                                                     (1, 'Renovare Școală Rurală', 'Renovare școală în mediul rural', '2024-05-10', '2024-06-10', 'Sat Florești', 'Florești', 'Cluj', 25, 'ACTIVE');

-- Competențe necesare pentru proiecte
INSERT INTO project_skills (project_id, skill_id, required_level, is_mandatory) VALUES
                                                                                    (1, 6, 'BEGINNER', TRUE),
                                                                                    (1, 11, 'INTERMEDIATE', TRUE),
                                                                                    (2, 7, 'INTERMEDIATE', TRUE),
                                                                                    (2, 10, 'INTERMEDIATE', TRUE),
                                                                                    (3, 11, 'BEGINNER', TRUE),
                                                                                    (4, 7, 'ADVANCED', TRUE),
                                                                                    (4, 3, 'INTERMEDIATE', TRUE),
                                                                                    (5, 6, 'INTERMEDIATE', TRUE),
                                                                                    (5, 4, 'INTERMEDIATE', FALSE);

-- Asignări
INSERT INTO assignments (volunteer_id, project_id, assignment_date, role, status) VALUES
                                                                                      (1, 2, '2024-02-05', 'Coordonator educație', 'ACCEPTED'),
                                                                                      (2, 3, '2024-04-10', 'Voluntar ecologizare', 'ACCEPTED'),
                                                                                      (3, 1, '2024-03-05', 'Coordonator IT', 'ACCEPTED'),
                                                                                      (4, 2, '2024-02-15', 'Asistent medical', 'ACCEPTED'),
                                                                                      (5, 4, '2024-06-15', 'Instructor prim ajutor', 'PENDING'),
                                                                                      (6, 1, '2024-03-10', 'Designer materiale', 'ACCEPTED'),
                                                                                      (7, 3, '2024-04-10', 'Fotograf oficial', 'ACCEPTED'),
                                                                                      (8, 2, '2024-02-20', 'Profesor voluntar', 'ACCEPTED');

-- Prezență (ore lucrate)
INSERT INTO attendance (assignment_id, date, hours_worked, notes, verified_by) VALUES
                                                                                   (1, '2024-02-05', 4.00, 'Prima zi - instruire', 'Coordinator Program'),
                                                                                   (1, '2024-02-06', 5.00, 'Lucru cu copiii grup A', 'Coordinator Program'),
                                                                                   (1, '2024-02-07', 5.00, 'Lucru cu copiii grup B', 'Coordinator Program'),
                                                                                   (2, '2024-04-15', 6.00, 'Ecologizare parc central', 'Manager Proiect'),
                                                                                   (3, '2024-03-05', 8.00, 'Setup sistem IT', 'Director Proiect'),
                                                                                   (3, '2024-03-06', 8.00, 'Training echipă', 'Director Proiect'),
                                                                                   (4, '2024-02-15', 4.00, 'Evaluare medicală copii', 'Coordinator Medical'),
                                                                                   (6, '2024-03-10', 6.00, 'Design logo și materiale', 'Manager Marketing'),
                                                                                   (7, '2024-04-15', 7.00, 'Fotografiere eveniment', 'Coordinator Media'),
                                                                                   (8, '2024-02-20', 5.00, 'Predare matematică', 'Coordinator Educație');

-- Certificate
INSERT INTO certificates (volunteer_id, project_id, certificate_number, issue_date, total_hours, description, signed_by) VALUES
                                                                                                                             (1, 2, 'CERT-2024-001', '2024-03-01', 14.00, 'Certificat de voluntariat - Program After-School', 'Director Asociație Pentru Valori'),
                                                                                                                             (2, 3, 'CERT-2024-002', '2024-04-16', 6.00, 'Certificat participare ecologizare', 'Director Greenpeace România'),
                                                                                                                             (3, 1, 'CERT-2024-003', '2024-04-01', 16.00, 'Certificat coordonare tehnică', 'Director Habitat for Humanity');

-- Evenimente
INSERT INTO events (project_id, title, description, event_date, location, max_participants, status) VALUES
                                                                                                        (1, 'Kick-off Construim Speranță', 'Eveniment de lansare proiect', '2024-03-01 10:00:00', 'Sala Polivalentă București', 100, 'COMPLETED'),
                                                                                                        (2, 'Ziua Porților Deschise', 'Prezentare program părinți', '2024-02-10 16:00:00', 'Școala Generală nr. 5', 50, 'COMPLETED'),
                                                                                                        (3, 'Curățăm Împreună', 'Acțiune de ecologizare', '2024-04-15 09:00:00', 'Parcul Central Timișoara', 50, 'COMPLETED'),
                                                                                                        (4, 'Întâlnire Voluntari Tabără', 'Pregătire tabără copii', '2024-06-20 18:00:00', 'Sediu Salvați Copiii', 20, 'SCHEDULED');

-- Participanți evenimente
INSERT INTO event_participants (event_id, volunteer_id, attendance_status) VALUES
                                                                               (1, 3, 'ATTENDED'),
                                                                               (1, 6, 'ATTENDED'),
                                                                               (2, 1, 'ATTENDED'),
                                                                               (2, 4, 'ATTENDED'),
                                                                               (2, 8, 'ATTENDED'),
                                                                               (3, 2, 'ATTENDED'),
                                                                               (3, 7, 'ATTENDED'),
                                                                               (4, 5, 'REGISTERED');

-- Feedback
INSERT INTO feedback (assignment_id, rating, comment, feedback_date, given_by, feedback_type) VALUES
                                                                                                  (1, 5, 'Voluntar excelent! Foarte dedicat și competent.', '2024-03-01', 'Coordinator Program', 'ORG_TO_VOLUNTEER'),
                                                                                                  (2, 4, 'Participare activă și entuziasm deosebit.', '2024-04-16', 'Manager Proiect', 'ORG_TO_VOLUNTEER'),
                                                                                                  (3, 5, 'Competențe tehnice excepționale. Recomand cu căldură!', '2024-04-01', 'Director Proiect', 'ORG_TO_VOLUNTEER'),
                                                                                                  (4, 5, 'Profesionalism și grijă pentru copii exemplare.', '2024-03-01', 'Coordinator Medical', 'ORG_TO_VOLUNTEER');
-- =================================================================
-- SCRIPT POPULARE BAZA DE DATE - Volunteer Management System
-- =================================================================
-- Acest script:
-- 1. Dezactiveaza temporar cheile straine
-- 2. Sterge (TRUNCATE) toate datele existente
-- 3. Insereaza date realiste si variate pentru testare
-- =================================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE feedback;
TRUNCATE TABLE event_participants;
TRUNCATE TABLE certificates;
TRUNCATE TABLE attendance;
TRUNCATE TABLE assignments;
TRUNCATE TABLE project_skills;
TRUNCATE TABLE volunteer_skills;
TRUNCATE TABLE events;
TRUNCATE TABLE projects;
TRUNCATE TABLE skills;
TRUNCATE TABLE volunteers;
TRUNCATE TABLE organizations;

SET FOREIGN_KEY_CHECKS = 1;

-- =================================================================
-- 1. ORGANIZATII (12 intrari diverse)
-- =================================================================
INSERT INTO organizations (id, name, description, email, phone, address, website, registration_number, status) VALUES
(1, 'EcoGreen Romania', 'Protejarea mediului inconjurator si impaduriri.', 'contact@ecogreen.ro', '0722111111', 'Str. Verde nr. 1, Bucuresti', 'https://ecogreen.ro', 'NGO-2023-001', 'ACTIVE'),
(2, 'Tech for Good', 'Educatie digitala pentru copii din medii defavorizate.', 'info@techforgood.ro', '0722222222', 'Bd. Unirii nr. 10, Cluj-Napoca', 'https://techforgood.ro', 'NGO-2023-002', 'ACTIVE'),
(3, 'Happy Paws Shelter', 'Adapost pentru animale fara stapan.', 'contact@happypaws.ro', '0722333333', 'Sos. Viilor nr. 5, Iasi', 'https://happypaws.ro', 'NGO-2023-003', 'ACTIVE'),
(4, 'Crucea Rosie Craiova', 'Ajutor umanitar si interventii in situatii de urgenta.', 'craiova@crucearosie.ro', '0722444444', 'Str. A.I. Cuza nr. 8, Craiova', 'https://crucearosie.ro', 'NGO-OLD-001', 'ACTIVE'),
(5, 'Asociatia Speranta', 'Sprijin pentru batrani singuri.', 'speranta@asociatia.ro', '0722555555', 'Str. Lunga nr. 20, Brasov', NULL, 'NGO-2023-005', 'ACTIVE'),
(6, 'Youth Voice', 'Promovarea implicarii civice a tinerilor.', 'office@youthvoice.ro', '0722666666', 'Str. Tineretului nr. 3, Timisoara', NULL, 'NGO-2023-006', 'PENDING'),
(7, 'Corporate Help Solutions', 'Departament CSR al companiei CHS.', 'csr@chs-corp.com', '0722777777', 'Business Park A, Bucuresti', 'https://chs-corp.com/csr', 'CORP-001', 'ACTIVE'),
(8, 'Scoala Generala Nr. 1', 'Proiecte educationale extrascolare.', 'scoala1@edu.ro', '0722888888', 'Str. Scolii nr. 1, Craiova', NULL, 'EDU-001', 'ACTIVE'),
(9, 'Festivalul Artelor', 'Organizare evenimente culturale.', 'contact@artfest.ro', '0722999999', 'Centrul Vechi, Sibiu', 'https://artfest.ro', 'NGO-CULT-01', 'INACTIVE'),
(10, 'Salvamont Romania', 'Salvare montana si prim ajutor.', 'office@salvamont.ro', '0722123123', 'Str. Muntelui, Brasov', 'https://salvamont.ro', 'GOV-001', 'ACTIVE'),
(11, 'Food Bank Bucharest', 'Colectare si distribuire alimente.', 'help@foodbank.ro', '0722321321', 'Sos. Garii, Bucuresti', 'https://foodbank.ro', 'NGO-FOOD-001', 'ACTIVE'),
(12, 'Code4Romania', 'Solutii digitale pentru societate.', 'contact@code4.ro', '0722987654', 'Hub Digital, Bucuresti', 'https://code4.ro', 'NGO-IT-001', 'ACTIVE');

-- =================================================================
-- 2. SKILLS (20 intrari)
-- =================================================================
INSERT INTO skills (id, name, description, category) VALUES
(1, 'Java Programming', 'Dezvoltare software backend', 'TECHNICAL'),
(2, 'React.js', 'Dezvoltare frontend', 'TECHNICAL'),
(3, 'Project Management', 'Coordonare echipe', 'MANAGEMENT'),
(4, 'English C1', 'Limba engleza nivel avansat', 'LANGUAGE'),
(5, 'First Aid', 'Prim ajutor de baza', 'OTHER'),
(6, 'Graphic Design', 'Adobe Photoshop/Illustrator', 'CREATIVE'),
(7, 'Public Speaking', 'Vorbit in public', 'SOCIAL'),
(8, 'Event Planning', 'Organizare logistica evenimente', 'MANAGEMENT'),
(9, 'Teaching', 'Abilitati pedagogice', 'SOCIAL'),
(10, 'Driving License B', 'Permis de conducere categoria B', 'OTHER'),
(11, 'Python', 'Scripting si Data Science', 'TECHNICAL'),
(12, 'Social Media Marketing', 'Promovare online', 'CREATIVE'),
(13, 'Photography', 'Fotografie eveniment', 'CREATIVE'),
(14, 'French B2', 'Limba franceza nivel mediu', 'LANGUAGE'),
(15, 'Communication', 'Comunicare eficienta', 'SOCIAL'),
(16, 'Fundraising', 'Strangere de fonduri', 'MANAGEMENT'),
(17, 'Cooking', 'Gatit cantitati mari', 'OTHER'),
(18, 'Video Editing', 'Premiere Pro / DaVinci', 'CREATIVE'),
(19, 'German A2', 'Limba germana incepator', 'LANGUAGE'),
(20, 'Mentoring', 'Consiliere si ghidare', 'SOCIAL');

-- =================================================================
-- 3. vOLUNTEERS (35 intrari)
-- =================================================================
INSERT INTO volunteers (id, first_name, last_name, email, phone, date_of_birth, cnp, address, city, county, registration_date, status, bio) VALUES
(1, 'Ion', 'Popescu', 'ion.popescu@email.com', '0700111111', '1990-05-15', '1900515123456', 'Str. Libertatii 1', 'Bucuresti', 'Bucuresti', '2023-01-10', 'ACTIVE', 'Pasionat de IT si voluntariat.'),
(2, 'Maria', 'Ionescu', 'maria.ionescu@email.com', '0700111112', '1995-08-20', '2950820123456', 'Str. Unirii 2', 'Cluj-Napoca', 'Cluj', '2023-02-15', 'ACTIVE', 'Studenta la medicina, vreau sa ajut.'),
(3, 'Andrei', 'Georgescu', 'andrei.geo@email.com', '0700111113', '1988-12-01', '1881201123456', NULL, 'Iasi', 'Iasi', '2023-01-20', 'ACTIVE', 'Inginer software, disponibil in weekend.'),
(4, 'Elena', 'Dumitrescu', 'elena.dumi@email.com', '0700111114', '2000-03-10', '6000310123456', 'Str. Florilor 5', 'Brasov', 'Brasov', '2023-03-01', 'ACTIVE', 'Imi place sa lucrez cu copiii.'),
(5, 'George', 'Mihai', 'george.mihai@email.com', '0700111115', '1999-07-25', '1990725123456', NULL, 'Craiova', 'Dolj', '2023-04-05', 'INACTIVE', 'Momentan ocupat cu facultatea.'),
(6, 'Ana', 'Stan', 'ana.stan@email.com', '0700111116', '1992-11-11', '2921111123456', 'Str. Pacii 10', 'Timisoara', 'Timis', '2023-01-15', 'ACTIVE', 'Experienta in PR si comunicare.'),
(7, 'Mihai', 'Radu', 'mihai.radu@email.com', '0700111117', '1985-02-28', '1850228123456', NULL, 'Constanta', 'Constanta', '2023-05-20', 'ACTIVE', 'Scafandru amator, salvamar.'),
(8, 'Diana', 'Stoica', 'diana.stoica@email.com', '0700111118', '1998-09-09', '2980909123456', 'Str. Soarelui 8', 'Bucuresti', 'Bucuresti', '2023-06-01', 'ACTIVE', 'Design grafic si fotografie.'),
(9, 'Alex', 'Matei', 'alex.matei@email.com', '0700111119', '2001-04-04', '5010404123456', NULL, 'Bucuresti', 'Bucuresti', '2023-07-10', 'BLOCKED', 'Nu a respectat regulamentul.'),
(10, 'Oana', 'Popa', 'oana.popa@email.com', '0700111120', '1996-01-30', '2960130123456', 'Str. Mare 100', 'Sibiu', 'Sibiu', '2023-02-28', 'ACTIVE', 'Organizata si punctuala.'),
(11, 'Cristian', 'Dobre', 'cristi.dobre@email.com', '0700111121', '1993-06-15', NULL, 'Str. Mica 2', 'Ploiesti', 'Prahova', '2023-08-01', 'ACTIVE', NULL),
(12, 'Laura', 'Enache', 'laura.enache@email.com', '0700111122', '1997-10-10', NULL, NULL, 'Oradea', 'Bihor', '2023-03-15', 'ACTIVE', NULL),
(13, 'Vlad', 'Diaconu', 'vlad.dia@email.com', '0700111123', '1991-05-05', NULL, NULL, 'Arad', 'Arad', '2023-09-01', 'ACTIVE', NULL),
(14, 'Simona', 'Ilie', 'simona.ilie@email.com', '0700111124', '1994-12-24', NULL, NULL, 'Pitesti', 'Arges', '2023-01-05', 'ACTIVE', NULL),
(15, 'Razvan', 'Tudor', 'razvan.tudor@email.com', '0700111125', '2002-01-01', NULL, NULL, 'Bucuresti', 'Bucuresti', '2023-10-10', 'ACTIVE', 'Student Politehnica.'),
(16, 'Ioana', 'Marin', 'ioana.marin@email.com', '0700111126', '1989-03-30', NULL, NULL, 'Cluj-Napoca', 'Cluj', '2023-02-10', 'ACTIVE', NULL),
(17, 'Gabriel', 'Lupu', 'gabi.lupu@email.com', '0700111127', '1990-07-07', NULL, NULL, 'Iasi', 'Iasi', '2023-04-20', 'ACTIVE', NULL),
(18, 'Alina', 'Nistor', 'alina.nist@email.com', '0700111128', '1995-11-15', NULL, NULL, 'Brasov', 'Brasov', '2023-05-05', 'ACTIVE', NULL),
(19, 'Florin', 'Dinu', 'florin.dinu@email.com', '0700111129', '1987-08-08', NULL, NULL, 'Timisoara', 'Timis', '2023-06-15', 'ACTIVE', NULL),
(20, 'Raluca', 'Serban', 'raluca.ser@email.com', '0700111130', '1999-02-14', NULL, NULL, 'Constanta', 'Constanta', '2023-07-20', 'ACTIVE', NULL),
(21, 'Dan', 'Barbu', 'dan.barbu@email.com', '0700111131', '1993-09-21', NULL, NULL, 'Craiova', 'Dolj', '2023-08-25', 'ACTIVE', NULL),
(22, 'Monica', 'Vasile', 'monica.v@email.com', '0700111132', '1996-04-12', NULL, NULL, 'Galati', 'Galati', '2023-01-30', 'ACTIVE', NULL),
(23, 'Savin', 'Costin', 'savin.c@email.com', '0700111133', '1991-12-05', NULL, NULL, 'Braila', 'Braila', '2023-02-25', 'ACTIVE', NULL),
(24, 'Carmen', 'Neagu', 'carmen.n@email.com', '0700111134', '1998-05-18', NULL, NULL, 'Baia Mare', 'Maramures', '2023-03-30', 'ACTIVE', NULL),
(25, 'Victor', 'Oprea', 'victor.o@email.com', '0700111135', '1994-10-22', NULL, NULL, 'Suceava', 'Suceava', '2023-09-15', 'ACTIVE', NULL),
(26, 'Adriana', 'Pop', 'adriana.p@email.com', '0700111136', '2000-01-19', NULL, NULL, 'Targu Mures', 'Mures', '2023-10-01', 'ACTIVE', NULL),
(27, 'Stefan', 'Ionita', 'stefan.i@email.com', '0700111137', '1992-07-03', NULL, NULL, 'Bacau', 'Bacau', '2023-11-11', 'ACTIVE', NULL),
(28, 'Roxana', 'Gheorghe', 'roxana.g@email.com', '0700111138', '1995-03-27', NULL, NULL, 'Botosani', 'Botosani', '2023-04-15', 'ACTIVE', NULL),
(29, 'Emil', 'Dima', 'emil.dima@email.com', '0700111139', '1986-06-16', NULL, NULL, 'Satu Mare', 'Satu Mare', '2023-05-10', 'ACTIVE', NULL),
(30, 'Corina', 'Avram', 'corina.a@email.com', '0700111140', '1997-09-02', NULL, NULL, 'Ramnicu Valcea', 'Valcea', '2023-06-05', 'ACTIVE', NULL),
(31, 'Bogdan', 'Rosu', 'bogdan.r@email.com', '0700111141', '1990-11-29', NULL, NULL, 'Drobeta Turnu Severin', 'Mehedinti', '2023-07-15', 'ACTIVE', NULL),
(32, 'Larisa', 'Munteanu', 'larisa.m@email.com', '0700111142', '1999-08-11', NULL, NULL, 'Piatra Neamt', 'Neamt', '2023-08-20', 'ACTIVE', NULL),
(33, 'Eugen', 'Stanciu', 'eugen.s@email.com', '0700111143', '1988-02-05', NULL, NULL, 'Targoviste', 'Dambovita', '2023-01-25', 'ACTIVE', NULL),
(34, 'Tatiana', 'Dragomir', 'tatiana.d@email.com', '0700111144', '1996-05-25', NULL, NULL, 'Focsani', 'Vrancea', '2023-02-18', 'ACTIVE', NULL),
(35, 'Horia', 'Preda', 'horia.p@email.com', '0700111145', '1993-12-12', NULL, NULL, 'Bistrita', 'Bistrita-Nasaud', '2023-03-22', 'ACTIVE', NULL);

-- =================================================================
-- 4. VOLUNTEER_SKILLS (Asocieri Skills - Randomizate manual)
-- =================================================================
INSERT INTO volunteer_skills (volunteer_id, skill_id, proficiency_level, years_experience) VALUES
(1, 1, 'ADVANCED', 5), (1, 3, 'BEGINNER', 1), -- Ion: Java, PM
(2, 5, 'INTERMEDIATE', 2), (2, 9, 'ADVANCED', 3), -- Maria: First Aid, Teaching
(3, 1, 'EXPERT', 8), (3, 2, 'ADVANCED', 5), (3, 11, 'INTERMEDIATE', 3), -- Andrei: Java, React, Python
(4, 9, 'INTERMEDIATE', 2), (4, 15, 'ADVANCED', 4), -- Elena: Teaching, Comm
(6, 7, 'ADVANCED', 5), (6, 12, 'EXPERT', 6), -- Ana: Public Speaking, Social Media
(7, 5, 'ADVANCED', 10), (7, 10, 'EXPERT', 15), -- Mihai: First Aid, Driving
(8, 6, 'ADVANCED', 4), (8, 13, 'INTERMEDIATE', 3), -- Diana: Graphic Design, Photo
(10, 8, 'ADVANCED', 5), (10, 16, 'INTERMEDIATE', 2), -- Oana: Event Planning, Fundraising
(11, 4, 'ADVANCED', 10), (12, 14, 'INTERMEDIATE', 5), (13, 19, 'BEGINNER', 2), -- Languages
(15, 1, 'BEGINNER', 1), (15, 11, 'BEGINNER', 1), -- Student IT
(20, 17, 'EXPERT', 5), (21, 10, 'EXPERT', 10), -- Cooking, Driving
(25, 2, 'INTERMEDIATE', 3), (25, 6, 'BEGINNER', 1),
(30, 15, 'EXPERT', 7);

-- =================================================================
-- 5. PROJECTS (18 proiecte)
-- =================================================================
INSERT INTO projects (id, organization_id, title, description, start_date, end_date, location, city, max_volunteers, status) VALUES
(1, 1, 'Impadurire Nationala 2024', 'Plantam 1000 de copaci.', '2024-03-15', '2024-03-20', 'Padurea Baneasa', 'Bucuresti', 50, 'ACTIVE'),
(2, 1, 'Curatenie in Parc', 'Strangem deseurile din parcuri.', '2023-05-01', '2023-05-01', 'Parc Central', 'Cluj-Napoca', 20, 'COMPLETED'),
(3, 2, 'Lectii de Programare', 'Cursuri Java pentru liceeni.', '2024-02-01', '2024-06-01', 'Online / Scoala 10', 'Bucuresti', 5, 'ACTIVE'),
(4, 2, 'Donatie Laptopuri', 'Colectare si reconditionare.', '2023-10-01', '2023-12-01', 'Sediu', 'Cluj-Napoca', 10, 'COMPLETED'),
(5, 3, 'Adopta un catel', 'Targ de adoptii.', '2024-04-10', '2024-04-12', 'Piata Unirii', 'Iasi', 15, 'DRAFT'),
(6, 3, 'Constructie Custi', 'Reparatii adapost iarna.', '2023-11-01', '2023-11-15', 'Adapost', 'Iasi', 10, 'COMPLETED'),
(7, 4, 'Curs Prim Ajutor', 'Instruire populatie.', '2024-01-20', '2024-01-20', 'Sala Polivalenta', 'Craiova', 30, 'COMPLETED'),
(8, 5, 'Vizite la Batrani', 'Socializare si ajutor cumparaturi.', '2024-01-01', '2024-12-31', 'Domiciliu beneficiari', 'Brasov', 20, 'ACTIVE'),
(9, 6, 'Dezbatere Tineret', 'Forum de discutii locale.', '2024-05-09', '2024-05-09', 'Centru Tineret', 'Timisoara', 50, 'DRAFT'),
(10, 7, 'Mentorat Cariera', 'Angajatii ajuta studentii.', '2023-09-01', '2023-12-30', 'Sediul CHS', 'Bucuresti', 15, 'COMPLETED'),
(11, 8, 'Afterschool Gratuit', 'Ajutor la teme.', '2024-02-15', '2024-06-15', 'Scoala 1', 'Craiova', 10, 'ACTIVE'),
(12, 10, 'Marcare Trasee', 'Refacere marcaje turistice.', '2024-06-01', '2024-08-01', 'Masivul Postavaru', 'Brasov', 25, 'DRAFT'),
(13, 11, 'Colecta Alimente Paste', 'Campanie supermarket.', '2024-04-25', '2024-05-04', 'Supermarketuri', 'Bucuresti', 100, 'DRAFT'),
(14, 12, 'Hackathon Civic', 'Aplicatii pentru oras.', '2023-11-20', '2023-11-22', 'Tech Hub', 'Bucuresti', 40, 'COMPLETED'),
(15, 12, 'Audit Digital', 'Verificare site-uri publice.', '2024-03-01', '2024-04-01', 'Remote', NULL, 10, 'ACTIVE'),
(16, 1, 'Atelier Reciclare', 'Invatam sa reciclam.', '2024-02-28', '2024-02-29', 'Mall', 'Bucuresti', 5, 'COMPLETED'),
(17, 4, 'Donare Sange', 'Campanie mobila.', '2024-03-10', '2024-03-10', 'Centru Transfuzii', 'Craiova', 10, 'COMPLETED'),
(18, 9, 'Festival Jazz', 'Suport logistic festival.', '2023-08-15', '2023-08-18', 'Piata Mare', 'Sibiu', 60, 'COMPLETED');

-- =================================================================
-- 6. PROJECT_SKILLS (Cerinte proiecte)
-- =================================================================
INSERT INTO project_skills (project_id, skill_id, required_level, is_mandatory) VALUES
(3, 1, 'INTERMEDIATE', 1), (3, 9, 'BEGINNER', 0), -- Lectii Java: Java, Teaching
(14, 1, 'ADVANCED', 0), (14, 2, 'ADVANCED', 0), (14, 11, 'INTERMEDIATE', 0), -- Hackathon: Tech skills
(7, 5, 'BEGINNER', 1), -- Prim Ajutor: First Aid
(12, 10, 'BEGINNER', 0), -- Marcare Trasee: Driving (pt transport)
(10, 3, 'ADVANCED', 1), -- Mentorat: Project Management
(18, 8, 'INTERMEDIATE', 1); -- Festival: Event Planning

-- =================================================================
-- 7. EVENTS (25 evenimente)
-- =================================================================
INSERT INTO events (project_id, title, description, event_date, location, status) VALUES
(2, 'Ziua Curateniei', 'Intalnire la intrarea principala.', '2023-05-01 09:00:00', 'Parc Central', 'COMPLETED'),
(3, 'Atelier Java 1', 'Intro in Java.', '2024-02-10 10:00:00', 'Online', 'COMPLETED'),
(3, 'Atelier Java 2', 'OOP Basics.', '2024-02-17 10:00:00', 'Online', 'COMPLETED'),
(3, 'Atelier Java 3', 'Colectii.', '2024-02-24 10:00:00', 'Online', 'COMPLETED'),
(7, 'Sesiune Teoretica', 'Prezentare slide-uri.', '2024-01-20 09:00:00', 'Sala Mica', 'COMPLETED'),
(7, 'Practica Manechim', 'Resuscitare.', '2024-01-20 13:00:00', 'Sala Mare', 'COMPLETED'),
(1, 'Plantare Lot 1', 'Zona Nord.', '2024-03-15 08:00:00', 'Padure', 'SCHEDULED'),
(1, 'Plantare Lot 2', 'Zona Sud.', '2024-03-16 08:00:00', 'Padure', 'SCHEDULED'),
(14, 'Hackathon Day 1', 'Coding marathon.', '2023-11-20 09:00:00', 'Hub', 'COMPLETED'),
(14, 'Hackathon Day 2', 'Coding marathon.', '2023-11-21 09:00:00', 'Hub', 'COMPLETED'),
(14, 'Hackathon Final', 'Prezentari.', '2023-11-22 18:00:00', 'Hub', 'COMPLETED'),
(10, 'Intalnire Kickoff', 'Cunoastere mentori.', '2023-09-01 18:00:00', 'CHS HQ', 'COMPLETED'),
(17, 'Ziua Donatorului', 'Toata ziua.', '2024-03-10 08:00:00', 'Spital', 'COMPLETED'),
(18, 'Montare Scena', 'Pregatiri.', '2023-08-14 10:00:00', 'Piata', 'COMPLETED'),
(18, 'Concert Seara 1', 'Accesul publicului.', '2023-08-15 19:00:00', 'Piata', 'COMPLETED'),
(18, 'Concert Seara 2', 'Accesul publicului.', '2023-08-16 19:00:00', 'Piata', 'COMPLETED'),
(18, 'Curatenie Post-Event', 'Demontare.', '2023-08-19 09:00:00', 'Piata', 'COMPLETED'),
(4, 'Receptie Laptopuri', 'Verificare stoc.', '2023-10-05 10:00:00', 'Sediu', 'COMPLETED'),
(4, 'Instalare Linux', 'Zi de instalare.', '2023-10-12 10:00:00', 'Sediu', 'COMPLETED'),
(16, 'Atelier Copii', 'Handmade reciclabil.', '2024-02-28 16:00:00', 'Mall', 'COMPLETED'),
(11, 'Meditatii Romano', 'Grupa 1.', '2024-02-20 14:00:00', 'Scoala', 'COMPLETED'),
(11, 'Meditatii Mate', 'Grupa 2.', '2024-02-22 14:00:00', 'Scoala', 'COMPLETED'),
(6, 'Reparatie Acoperis', 'Echipa tehnica.', '2023-11-02 09:00:00', 'Adapost', 'COMPLETED'),
(12, 'Inspectie Traseu', 'Doar coordonatorii.', '2024-05-15 08:00:00', 'Munte', 'SCHEDULED'),
(13, 'Briefing Voluntari', 'Instructaj.', '2024-04-20 18:00:00', 'Zoom', 'SCHEDULED');

-- =================================================================
-- 8. ASSIGNMENTS & 9. ATTENDANCE (50+ asignari, 100+ prezente)
-- =================================================================
-- Proiect 2: Curatenie (Completed)
INSERT INTO assignments (id, volunteer_id, project_id, status, assignment_date) VALUES
(1, 1, 2, 'COMPLETED', '2023-04-20'),
(2, 2, 2, 'COMPLETED', '2023-04-21'),
(3, 4, 2, 'COMPLETED', '2023-04-22'),
(4, 5, 2, 'CANCELLED', '2023-04-20'),
(5, 6, 2, 'COMPLETED', '2023-04-25');

INSERT INTO attendance (assignment_id, date, hours_worked, verified_by) VALUES
(1, '2023-05-01', 4, 'Admin'), (2, '2023-05-01', 4, 'Admin'), (3, '2023-05-01', 4, 'Admin'), (5, '2023-05-01', 4, 'Admin');

-- Proiect 7: Curs Prim Ajutor (Completed)
INSERT INTO assignments (volunteer_id, project_id, status, assignment_date) VALUES
(1, 7, 'COMPLETED', '2024-01-10'), (2, 7, 'COMPLETED', '2024-01-10'), (4, 7, 'COMPLETED', '2024-01-11'), 
(6, 7, 'COMPLETED', '2024-01-10'), (7, 7, 'COMPLETED', '2024-01-12'), (8, 7, 'PENDING', '2024-01-18');

INSERT INTO attendance (assignment_id, date, hours_worked) SELECT id, '2024-01-20', 6 FROM assignments WHERE project_id=7 AND status='COMPLETED';

-- Proiect 14: Hackathon (Completed)
INSERT INTO assignments (volunteer_id, project_id, status, assignment_date) VALUES
(3, 14, 'COMPLETED', '2023-11-01'), (15, 14, 'COMPLETED', '2023-11-05'), (1, 14, 'COMPLETED', '2023-11-02');
INSERT INTO attendance (assignment_id, date, hours_worked) VALUES
(12, '2023-11-20', 10), (12, '2023-11-21', 10), (12, '2023-11-22', 8), -- Vol 3
(13, '2023-11-20', 10), (13, '2023-11-21', 5), -- Vol 15
(14, '2023-11-20', 8); -- Vol 1

-- Proiect 3: Lectii (Active)
INSERT INTO assignments (volunteer_id, project_id, status, assignment_date) VALUES
(1, 3, 'ACCEPTED', '2024-01-15'), (4, 3, 'PENDING', '2024-01-20');
INSERT INTO attendance (assignment_id, date, hours_worked) VALUES
(15, '2024-02-10', 2), (15, '2024-02-17', 2), (15, '2024-02-24', 2); -- Vol 1

-- Proiect 18: Festival Jazz (Completed, multi voluntari)
INSERT INTO assignments (volunteer_id, project_id, status, assignment_date) VALUES
(6, 18, 'COMPLETED', '2023-08-01'), (8, 18, 'COMPLETED', '2023-08-01'), (10, 18, 'COMPLETED', '2023-08-02'),
(11, 18, 'COMPLETED', '2023-08-05'), (12, 18, 'COMPLETED', '2023-08-05'), (13, 18, 'REJECTED', '2023-08-10');
INSERT INTO attendance (assignment_id, date, hours_worked) VALUES
(17, '2023-08-15', 8), (17, '2023-08-16', 8), -- Vol 6
(18, '2023-08-15', 10), -- Vol 8
(19, '2023-08-14', 12); -- Vol 10

-- Random assignments
INSERT INTO assignments (volunteer_id, project_id, status, assignment_date) VALUES
(20, 11, 'ACCEPTED', '2024-02-01'), (21, 11, 'ACCEPTED', '2024-02-01'),
(22, 1, 'ACCEPTED', '2024-02-20'), (23, 1, 'ACCEPTED', '2024-02-20'),
(24, 12, 'PENDING', '2024-05-01'), (25, 12, 'PENDING', '2024-05-01'),
(26, 13, 'PENDING', '2024-04-20'),
(27, 4, 'COMPLETED', '2023-10-01'), (28, 4, 'COMPLETED', '2023-10-01'),
(29, 6, 'COMPLETED', '2023-11-01');

INSERT INTO attendance (assignment_id, date, hours_worked) VALUES
(29, '2023-10-05', 4), (29, '2023-10-12', 6),
(30, '2023-10-12', 6),
(31, '2023-11-02', 8);

-- =================================================================
-- 10. FEEDBACK (30 intrari)
-- =================================================================
INSERT INTO feedback (assignment_id, rating, comment, feedback_date, feedback_type) VALUES
(1, 5, 'Super harnic!', '2023-05-02', 'ORG_TO_VOLUNTEER'),
(1, 5, 'Mi-a placut mult atmosfera.', '2023-05-03', 'VOLUNTEER_TO_ORG'),
(2, 4, 'S-a descurcat bine.', '2023-05-02', 'ORG_TO_VOLUNTEER'),
(3, 5, 'Exceptional.', '2023-05-02', 'ORG_TO_VOLUNTEER'),
(12, 5, 'Un geniu al codului.', '2023-11-25', 'ORG_TO_VOLUNTEER'),
(12, 4, 'Eveniment bine organizat, dar mancarea slaba.', '2023-11-26', 'VOLUNTEER_TO_ORG'),
(17, 5, 'Foarte implicata in comunicare.', '2023-08-20', 'ORG_TO_VOLUNTEER'),
(4, 1, 'Nu a venit desi a confirmat.', '2023-04-21', 'ORG_TO_VOLUNTEER'),
(15, 5, 'Profesor excelent.', '2024-02-25', 'ORG_TO_VOLUNTEER'),
(29, 5, 'De mare ajutor la instalare.', '2023-10-15', 'ORG_TO_VOLUNTEER'),
(31, 5, 'Munca grea dar satisfacatoare.', '2023-11-05', 'VOLUNTEER_TO_ORG');

-- =================================================================
-- 11. CERTIFICATES (20 intrari)
-- =================================================================
INSERT INTO certificates (volunteer_id, project_id, certificate_number, issue_date, total_hours, description) VALUES
(1, 2, 'CERT-2023-001', '2023-05-10', 4, 'Participare activitate ecologizare.'),
(2, 2, 'CERT-2023-002', '2023-05-10', 4, 'Participare activitate ecologizare.'),
(4, 2, 'CERT-2023-003', '2023-05-10', 4, 'Participare activitate ecologizare.'),
(6, 2, 'CERT-2023-004', '2023-05-10', 4, 'Participare activitate ecologizare.'),
(3, 14, 'CERT-2023-005', '2023-11-30', 28, 'Participare Hackathon Civic.'),
(15, 14, 'CERT-2023-006', '2023-11-30', 15, 'Participare Hackathon Civic.'),
(1, 14, 'CERT-2023-007', '2023-11-30', 8, 'Mentorat Hackathon Civic.'),
(6, 18, 'CERT-2023-008', '2023-08-25', 16, 'Voluntar Festival Jazz.'),
(8, 18, 'CERT-2023-009', '2023-08-25', 10, 'Fotograf Festival Jazz.'),
(10, 18, 'CERT-2023-010', '2023-08-25', 12, 'Logistica Festival Jazz.'),
(1, 7, 'CERT-2024-001', '2024-01-25', 6, 'Curs Prim Ajutor absolvit.'),
(2, 7, 'CERT-2024-002', '2024-01-25', 6, 'Curs Prim Ajutor absolvit.');

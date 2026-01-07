-- ============================================
-- VOLUNTEER MANAGEMENT SYSTEM - DATABASE SCHEMA
-- ============================================

-- Ștergere tabele existente (în ordine inversă)
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS event_participants;
DROP TABLE IF EXISTS certificates;
DROP TABLE IF EXISTS attendance;
DROP TABLE IF EXISTS assignments;
DROP TABLE IF EXISTS project_skills;
DROP TABLE IF EXISTS volunteer_skills;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS volunteers;
DROP TABLE IF EXISTS organizations;

-- ============================================
-- TABEL 1: ORGANIZATIONS (Organizații)
-- ============================================
CREATE TABLE organizations (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(200) NOT NULL,
                               description TEXT,
                               email VARCHAR(100) NOT NULL UNIQUE,
                               phone VARCHAR(20),
                               address VARCHAR(300),
                               website VARCHAR(200),
                               registration_number VARCHAR(50) UNIQUE,
                               status ENUM('ACTIVE', 'INACTIVE', 'PENDING') DEFAULT 'ACTIVE',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               INDEX idx_org_status (status),
                               INDEX idx_org_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 2: VOLUNTEERS (Voluntari)
-- ============================================
CREATE TABLE volunteers (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            first_name VARCHAR(100) NOT NULL,
                            last_name VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL UNIQUE,
                            phone VARCHAR(20),
                            date_of_birth DATE,
                            cnp VARCHAR(13) UNIQUE,
                            address VARCHAR(300),
                            city VARCHAR(100),
                            county VARCHAR(100),
                            registration_date DATE NOT NULL,
                            status ENUM('ACTIVE', 'INACTIVE', 'BLOCKED') DEFAULT 'ACTIVE',
                            avatar_url VARCHAR(500),
                            bio TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            INDEX idx_volunteer_email (email),
                            INDEX idx_volunteer_status (status),
                            INDEX idx_volunteer_name (last_name, first_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 3: SKILLS (Competențe)
-- ============================================
CREATE TABLE skills (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        description TEXT,
                        category ENUM('TECHNICAL', 'SOCIAL', 'MANAGEMENT', 'CREATIVE', 'LANGUAGE', 'OTHER') DEFAULT 'OTHER',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        INDEX idx_skill_category (category),
                        INDEX idx_skill_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 4: VOLUNTEER_SKILLS (Relație Many-to-Many)
-- ============================================
CREATE TABLE volunteer_skills (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  volunteer_id BIGINT NOT NULL,
                                  skill_id BIGINT NOT NULL,
                                  proficiency_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') DEFAULT 'BEGINNER',
                                  years_experience INT DEFAULT 0,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (volunteer_id) REFERENCES volunteers(id) ON DELETE CASCADE,
                                  FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
                                  UNIQUE KEY unique_volunteer_skill (volunteer_id, skill_id),
                                  INDEX idx_vs_volunteer (volunteer_id),
                                  INDEX idx_vs_skill (skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 5: PROJECTS (Proiecte)
-- ============================================
CREATE TABLE projects (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          organization_id BIGINT NOT NULL,
                          title VARCHAR(200) NOT NULL,
                          description TEXT,
                          start_date DATE NOT NULL,
                          end_date DATE,
                          location VARCHAR(300),
                          city VARCHAR(100),
                          county VARCHAR(100),
                          max_volunteers INT DEFAULT 10,
                          current_volunteers INT DEFAULT 0,
                          status ENUM('DRAFT', 'ACTIVE', 'COMPLETED', 'CANCELLED') DEFAULT 'DRAFT',
                          image_url VARCHAR(500),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
                          INDEX idx_project_org (organization_id),
                          INDEX idx_project_status (status),
                          INDEX idx_project_dates (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 6: PROJECT_SKILLS (Competențe necesare pentru proiecte)
-- ============================================
CREATE TABLE project_skills (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                project_id BIGINT NOT NULL,
                                skill_id BIGINT NOT NULL,
                                required_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') DEFAULT 'BEGINNER',
                                is_mandatory BOOLEAN DEFAULT FALSE,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                                FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
                                UNIQUE KEY unique_project_skill (project_id, skill_id),
                                INDEX idx_ps_project (project_id),
                                INDEX idx_ps_skill (skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 7: ASSIGNMENTS (Asignări voluntar-proiect)
-- ============================================
CREATE TABLE assignments (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             volunteer_id BIGINT NOT NULL,
                             project_id BIGINT NOT NULL,
                             assignment_date DATE NOT NULL,
                             role VARCHAR(100),
                             status ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
                             notes TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (volunteer_id) REFERENCES volunteers(id) ON DELETE CASCADE,
                             FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                             INDEX idx_assignment_volunteer (volunteer_id),
                             INDEX idx_assignment_project (project_id),
                             INDEX idx_assignment_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 8: ATTENDANCE (Prezență/Ore lucrate)
-- ============================================
CREATE TABLE attendance (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            assignment_id BIGINT NOT NULL,
                            date DATE NOT NULL,
                            hours_worked DECIMAL(4,2) NOT NULL DEFAULT 0.00,
                            notes TEXT,
                            verified_by VARCHAR(100),
                            verified_at TIMESTAMP NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
                            INDEX idx_attendance_assignment (assignment_id),
                            INDEX idx_attendance_date (date),
                            CHECK (hours_worked >= 0 AND hours_worked <= 24)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 9: CERTIFICATES (Certificate/Adeverințe)
-- ============================================
CREATE TABLE certificates (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              volunteer_id BIGINT NOT NULL,
                              project_id BIGINT,
                              certificate_number VARCHAR(50) UNIQUE NOT NULL,
                              issue_date DATE NOT NULL,
                              total_hours DECIMAL(6,2) NOT NULL,
                              description TEXT,
                              signed_by VARCHAR(100),
                              file_url VARCHAR(500),
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (volunteer_id) REFERENCES volunteers(id) ON DELETE CASCADE,
                              FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE SET NULL,
                              INDEX idx_cert_volunteer (volunteer_id),
                              INDEX idx_cert_number (certificate_number),
                              INDEX idx_cert_date (issue_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 10: EVENTS (Evenimente)
-- ============================================
CREATE TABLE events (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        project_id BIGINT NOT NULL,
                        title VARCHAR(200) NOT NULL,
                        description TEXT,
                        event_date DATETIME NOT NULL,
                        location VARCHAR(300),
                        max_participants INT DEFAULT 50,
                        current_participants INT DEFAULT 0,
                        status ENUM('SCHEDULED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
                        INDEX idx_event_project (project_id),
                        INDEX idx_event_date (event_date),
                        INDEX idx_event_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 11: EVENT_PARTICIPANTS (Participanți evenimente)
-- ============================================
CREATE TABLE event_participants (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    event_id BIGINT NOT NULL,
                                    volunteer_id BIGINT NOT NULL,
                                    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    attendance_status ENUM('REGISTERED', 'ATTENDED', 'ABSENT', 'CANCELLED') DEFAULT 'REGISTERED',
                                    notes TEXT,
                                    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
                                    FOREIGN KEY (volunteer_id) REFERENCES volunteers(id) ON DELETE CASCADE,
                                    UNIQUE KEY unique_event_volunteer (event_id, volunteer_id),
                                    INDEX idx_ep_event (event_id),
                                    INDEX idx_ep_volunteer (volunteer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABEL 12: FEEDBACK (Evaluări/Feedback)
-- ============================================
CREATE TABLE feedback (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          assignment_id BIGINT NOT NULL,
                          rating INT NOT NULL,
                          comment TEXT,
                          feedback_date DATE NOT NULL,
                          given_by VARCHAR(100),
                          feedback_type ENUM('VOLUNTEER_TO_ORG', 'ORG_TO_VOLUNTEER') DEFAULT 'ORG_TO_VOLUNTEER',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (assignment_id) REFERENCES assignments(id) ON DELETE CASCADE,
                          INDEX idx_feedback_assignment (assignment_id),
                          INDEX idx_feedback_rating (rating),
                          CHECK (rating >= 1 AND rating <= 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
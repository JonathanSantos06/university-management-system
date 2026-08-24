-- =====================================================================
-- SGU - Sistema de Gestión Universitaria
-- schema.sql  |  PostgreSQL 17
-- Cada microservicio es dueño de su esquema. No hay FK físicas entre
-- esquemas (simulan límites de servicio); las referencias cruzadas se
-- resuelven por ID + validación a nivel de aplicación (o eventos).
-- =====================================================================

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =====================================================================
-- SCHEMA: auth  (auth-service)
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.roles (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(50)  NOT NULL UNIQUE,   -- ADMIN, ALUMNO, PERSONAL_ADMINISTRATIVO...
    description VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE auth.permissions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,   -- STUDENT_READ, DOCUMENT_VALIDATE...
    description VARCHAR(255)
);

CREATE TABLE auth.role_permissions (
    role_id       UUID NOT NULL REFERENCES auth.roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES auth.permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE auth.users (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active     BOOLEAN NOT NULL DEFAULT TRUE,
    is_locked     BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_auth_users_email ON auth.users(email);

CREATE TABLE auth.user_roles (
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES auth.roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE auth.refresh_tokens (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    token_hash  VARCHAR(255) NOT NULL UNIQUE,
    expires_at  TIMESTAMPTZ NOT NULL,
    revoked     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_auth_refresh_user ON auth.refresh_tokens(user_id);

-- =====================================================================
-- SCHEMA: student  (student-service)
-- user_id / career_id / academic_period_id son referencias lógicas
-- a otros servicios (auth-service / academic-service), sin FK física.
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS student;

CREATE TABLE student.students (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL UNIQUE,          -- ref -> auth.users.id
    student_code        VARCHAR(20) NOT NULL UNIQUE,    -- matrícula
    career_id           UUID NOT NULL,                  -- ref -> academic.careers.id
    admission_period_id UUID NOT NULL,                  -- ref -> academic.academic_periods.id
    current_semester    SMALLINT NOT NULL DEFAULT 1 CHECK (current_semester BETWEEN 1 AND 20),
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
                        CHECK (status IN ('ACTIVO','BAJA_TEMPORAL','BAJA_DEFINITIVA','EGRESADO','TITULADO')),
    created_at          TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_student_career ON student.students(career_id);
CREATE INDEX idx_student_status ON student.students(status);

CREATE TABLE student.student_personal_data (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id        UUID NOT NULL UNIQUE REFERENCES student.students(id) ON DELETE CASCADE, -- 1:1
    first_name        VARCHAR(80) NOT NULL,
    last_name_paternal VARCHAR(80) NOT NULL,
    last_name_maternal VARCHAR(80),
    birth_date        DATE NOT NULL,
    gender            VARCHAR(20),
    curp              VARCHAR(18) NOT NULL UNIQUE,
    rfc               VARCHAR(13),
    nationality       VARCHAR(50) NOT NULL DEFAULT 'MEXICANA',
    phone             VARCHAR(20),
    personal_email    VARCHAR(150),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE student.addresses (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id   UUID NOT NULL REFERENCES student.students(id) ON DELETE CASCADE, -- 1:N
    address_type VARCHAR(20) NOT NULL DEFAULT 'ACTUAL' CHECK (address_type IN ('ACTUAL','PERMANENTE')),
    street       VARCHAR(150) NOT NULL,
    ext_number   VARCHAR(10),
    int_number   VARCHAR(10),
    neighborhood VARCHAR(100),
    city         VARCHAR(100) NOT NULL,
    state        VARCHAR(100) NOT NULL,
    postal_code  VARCHAR(10) NOT NULL,
    country      VARCHAR(60) NOT NULL DEFAULT 'MÉXICO',
    UNIQUE (student_id, address_type)
);
CREATE INDEX idx_addresses_student ON student.addresses(student_id);

CREATE TABLE student.emergency_contacts (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id   UUID NOT NULL REFERENCES student.students(id) ON DELETE CASCADE, -- 1:N
    full_name    VARCHAR(150) NOT NULL,
    relationship VARCHAR(50)  NOT NULL,
    phone        VARCHAR(20)  NOT NULL,
    email        VARCHAR(150)
);
CREATE INDEX idx_emergency_student ON student.emergency_contacts(student_id);

-- =====================================================================
-- SCHEMA: academic  (academic-service)
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS academic;

CREATE TABLE academic.careers (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name             VARCHAR(150) NOT NULL UNIQUE,
    code             VARCHAR(20)  NOT NULL UNIQUE,
    description      TEXT,
    total_semesters  SMALLINT NOT NULL CHECK (total_semesters > 0),
    is_active        BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE academic.academic_periods (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(50) NOT NULL UNIQUE,      -- '2026-1'
    code       VARCHAR(20) NOT NULL UNIQUE,
    start_date DATE NOT NULL,
    end_date   DATE NOT NULL,
    is_active  BOOLEAN NOT NULL DEFAULT FALSE,
    CHECK (end_date > start_date)
);

CREATE TABLE academic.subjects (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name           VARCHAR(150) NOT NULL,
    code           VARCHAR(20)  NOT NULL UNIQUE,
    credits        SMALLINT NOT NULL CHECK (credits >= 0),
    hours_theory   SMALLINT NOT NULL DEFAULT 0,
    hours_practice SMALLINT NOT NULL DEFAULT 0,
    is_active      BOOLEAN NOT NULL DEFAULT TRUE
);

-- Retícula: materia x carrera x semestre  (N:M careers<->subjects con atributos)
CREATE TABLE academic.career_subjects (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    career_id    UUID NOT NULL REFERENCES academic.careers(id) ON DELETE CASCADE,
    subject_id   UUID NOT NULL REFERENCES academic.subjects(id) ON DELETE CASCADE,
    semester     SMALLINT NOT NULL CHECK (semester BETWEEN 1 AND 20),
    is_mandatory BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (career_id, subject_id)
);
CREATE INDEX idx_career_subjects_career ON academic.career_subjects(career_id, semester);

-- Prerrequisitos: N:M auto-referenciada sobre subjects
CREATE TABLE academic.subject_prerequisites (
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subject_id             UUID NOT NULL REFERENCES academic.subjects(id) ON DELETE CASCADE,
    prerequisite_subject_id UUID NOT NULL REFERENCES academic.subjects(id) ON DELETE CASCADE,
    UNIQUE (subject_id, prerequisite_subject_id),
    CHECK (subject_id <> prerequisite_subject_id)
);

-- Inscripción de un alumno a un periodo
CREATE TABLE academic.enrollments (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id          UUID NOT NULL,     -- ref -> student.students.id
    academic_period_id  UUID NOT NULL REFERENCES academic.academic_periods(id),
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'
                        CHECK (status IN ('ACTIVA','CERRADA','CANCELADA')),
    enrollment_date     TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (student_id, academic_period_id)
);
CREATE INDEX idx_enrollments_student ON academic.enrollments(student_id);

-- Materias dentro de una inscripción
CREATE TABLE academic.enrollment_subjects (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enrollment_id  UUID NOT NULL REFERENCES academic.enrollments(id) ON DELETE CASCADE,
    subject_id     UUID NOT NULL REFERENCES academic.subjects(id),
    group_code     VARCHAR(10) NOT NULL DEFAULT 'A',
    status         VARCHAR(20) NOT NULL DEFAULT 'CURSANDO'
                   CHECK (status IN ('CURSANDO','APROBADA','REPROBADA','BAJA')),
    UNIQUE (enrollment_id, subject_id)
);
CREATE INDEX idx_enrollment_subjects_enrollment ON academic.enrollment_subjects(enrollment_id);

-- Calificaciones (parciales) por materia inscrita
CREATE TABLE academic.grades (
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    enrollment_subject_id  UUID NOT NULL REFERENCES academic.enrollment_subjects(id) ON DELETE CASCADE,
    partial_number         SMALLINT NOT NULL CHECK (partial_number BETWEEN 1 AND 4), -- 4 = final
    grade_value            NUMERIC(4,1) NOT NULL CHECK (grade_value BETWEEN 0 AND 10),
    recorded_by            UUID,           -- ref -> auth.users.id
    recorded_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (enrollment_subject_id, partial_number)
);
CREATE INDEX idx_grades_enrollment_subject ON academic.grades(enrollment_subject_id);

-- =====================================================================
-- SCHEMA: document  (document-service)
-- =====================================================================
CREATE SCHEMA IF NOT EXISTS document;

CREATE TABLE document.document_types (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,   -- Acta de nacimiento, CURP, ...
    code        VARCHAR(30)  NOT NULL UNIQUE,
    is_required BOOLEAN NOT NULL DEFAULT TRUE,
    description VARCHAR(255)
);

CREATE TABLE document.student_documents (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    student_id        UUID NOT NULL,        -- ref -> student.students.id
    document_type_id  UUID NOT NULL REFERENCES document.document_types(id),
    file_name         VARCHAR(255) NOT NULL,
    file_path         VARCHAR(500) NOT NULL,
    mime_type         VARCHAR(100) NOT NULL,
    status            VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                      CHECK (status IN ('PENDIENTE','VALIDADO','RECHAZADO')),
    uploaded_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    reviewed_by       UUID,                  -- ref -> auth.users.id
    reviewed_at       TIMESTAMPTZ,
    rejection_reason  VARCHAR(255)
);
CREATE INDEX idx_student_documents_student ON document.student_documents(student_id);
CREATE INDEX idx_student_documents_status  ON document.student_documents(status);

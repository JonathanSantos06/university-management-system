-- =====================================================================
-- data.sql - Datos de prueba
-- =====================================================================

-- ---------------------------------------------------------------------
-- USERS + USER_ROLES
-- ---------------------------------------------------------------------
INSERT INTO auth.users (id, username, email, password_hash, is_active) VALUES
 ('11111111-1111-1111-1111-111111111111', 'admin', 'admin@sgu.edu.mx',
  '$2b$10$cTTeU.abgh4gs/rSuIqKKujOYExBLSaLcbVp5lmrs2Qwo8IMcKmye', TRUE),
 ('22222222-2222-2222-2222-222222222222', 'jperez', 'jperez@alumnos.sgu.edu.mx',
  '$2b$10$cTTeU.abgh4gs/rSuIqKKujOYExBLSaLcbVp5lmrs2Qwo8IMcKmye', TRUE),
 ('33333333-3333-3333-3333-333333333333', 'mlopez', 'mlopez@alumnos.sgu.edu.mx',
  '$2b$10$cTTeU.abgh4gs/rSuIqKKujOYExBLSaLcbVp5lmrs2Qwo8IMcKmye', TRUE),
 ('44444444-4444-4444-4444-444444444444', 'control1', 'control1@sgu.edu.mx',
  '$2b$10$cTTeU.abgh4gs/rSuIqKKujOYExBLSaLcbVp5lmrs2Qwo8IMcKmye', TRUE);
-- Password de TODOS los usuarios demo: "Password123!"

INSERT INTO auth.user_roles (user_id, role_id)
 SELECT '11111111-1111-1111-1111-111111111111', id FROM auth.roles WHERE name='ADMIN';
INSERT INTO auth.user_roles (user_id, role_id)
 SELECT '22222222-2222-2222-2222-222222222222', id FROM auth.roles WHERE name='ALUMNO';
INSERT INTO auth.user_roles (user_id, role_id)
 SELECT '33333333-3333-3333-3333-333333333333', id FROM auth.roles WHERE name='ALUMNO';
INSERT INTO auth.user_roles (user_id, role_id)
 SELECT '44444444-4444-4444-4444-444444444444', id FROM auth.roles WHERE name='PERSONAL_ADMINISTRATIVO';

-- ---------------------------------------------------------------------
-- SUBJECTS (ISC - primeros semestres)
-- ---------------------------------------------------------------------
INSERT INTO academic.subjects (id, name, code, credits, hours_theory, hours_practice) VALUES
 ('a1000000-0000-0000-0000-000000000001', 'Programación I', 'ISC-101', 8, 3, 2),
 ('a1000000-0000-0000-0000-000000000002', 'Matemáticas Discretas', 'ISC-102', 7, 4, 0),
 ('a1000000-0000-0000-0000-000000000003', 'Fundamentos de Redes', 'ISC-103', 6, 3, 1),
 ('a1000000-0000-0000-0000-000000000004', 'Programación II', 'ISC-201', 8, 3, 2),
 ('a1000000-0000-0000-0000-000000000005', 'Estructura de Datos', 'ISC-202', 8, 3, 2),
 ('a1000000-0000-0000-0000-000000000006', 'Bases de Datos I', 'ISC-203', 8, 3, 2);

-- Prerrequisitos: Programación II requiere Programación I; Estructura de Datos requiere Programación II
INSERT INTO academic.subject_prerequisites (subject_id, prerequisite_subject_id) VALUES
 ('a1000000-0000-0000-0000-000000000004', 'a1000000-0000-0000-0000-000000000001'),
 ('a1000000-0000-0000-0000-000000000005', 'a1000000-0000-0000-0000-000000000004');

-- ---------------------------------------------------------------------
-- CAREER_SUBJECTS (retícula ISC semestres 1 y 2)
-- ---------------------------------------------------------------------
INSERT INTO academic.career_subjects (career_id, subject_id, semester, is_mandatory)
 SELECT c.id, s.id, 1, TRUE FROM academic.careers c, academic.subjects s
 WHERE c.code='ISC' AND s.code IN ('ISC-101','ISC-102','ISC-103');
INSERT INTO academic.career_subjects (career_id, subject_id, semester, is_mandatory)
 SELECT c.id, s.id, 2, TRUE FROM academic.careers c, academic.subjects s
 WHERE c.code='ISC' AND s.code IN ('ISC-201','ISC-202','ISC-203');

-- ---------------------------------------------------------------------
-- STUDENTS + PERSONAL DATA + ADDRESS + EMERGENCY CONTACT
-- ---------------------------------------------------------------------
INSERT INTO student.students (id, user_id, student_code, career_id, admission_period_id, current_semester, status)
 SELECT 'b1000000-0000-0000-0000-000000000001', '22222222-2222-2222-2222-222222222222',
        '2025ISC0001', c.id, p.id, 2, 'ACTIVO'
 FROM academic.careers c, academic.academic_periods p
 WHERE c.code='ISC' AND p.code='2025-2';

INSERT INTO student.students (id, user_id, student_code, career_id, admission_period_id, current_semester, status)
 SELECT 'b1000000-0000-0000-0000-000000000002', '33333333-3333-3333-3333-333333333333',
        '2025LAE0001', c.id, p.id, 2, 'ACTIVO'
 FROM academic.careers c, academic.academic_periods p
 WHERE c.code='LAE' AND p.code='2025-2';

INSERT INTO student.student_personal_data
 (student_id, first_name, last_name_paternal, last_name_maternal, birth_date, gender, curp, rfc, phone, personal_email)
VALUES
 ('b1000000-0000-0000-0000-000000000001', 'Juan', 'Pérez', 'Gómez', '2006-03-14', 'MASCULINO',
  'PEGJ060314HDFRZN08', 'PEGJ060314XXX', '5512345678', 'juan.perez@gmail.com'),
 ('b1000000-0000-0000-0000-000000000002', 'María', 'López', 'Hernández', '2005-11-02', 'FEMENINO',
  'LOHM051102MDFPRR03', 'LOHM051102XXX', '5598765432', 'maria.lopez@gmail.com');

INSERT INTO student.addresses (student_id, address_type, street, ext_number, neighborhood, city, state, postal_code)
VALUES
 ('b1000000-0000-0000-0000-000000000001', 'ACTUAL', 'Av. Reforma', '123', 'Centro', 'Ciudad de México', 'CDMX', '06000'),
 ('b1000000-0000-0000-0000-000000000002', 'ACTUAL', 'Calle Juárez', '45', 'Del Valle', 'Ciudad de México', 'CDMX', '03100');

INSERT INTO student.emergency_contacts (student_id, full_name, relationship, phone)
VALUES
 ('b1000000-0000-0000-0000-000000000001', 'Rosa Gómez', 'Madre', '5511223344'),
 ('b1000000-0000-0000-0000-000000000002', 'Carlos López', 'Padre', '5599887766');

-- ---------------------------------------------------------------------
-- ENROLLMENT (periodo 2025-2, semestre 1 ya cursado)
-- ---------------------------------------------------------------------
INSERT INTO academic.enrollments (id, student_id, academic_period_id, status)
 SELECT 'c1000000-0000-0000-0000-000000000001', 'b1000000-0000-0000-0000-000000000001', p.id, 'CERRADA'
 FROM academic.academic_periods p WHERE p.code='2025-2';

INSERT INTO academic.enrollment_subjects (id, enrollment_id, subject_id, group_code, status) VALUES
 ('d1000000-0000-0000-0000-000000000001','c1000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000001','A','APROBADA'),
 ('d1000000-0000-0000-0000-000000000002','c1000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000002','A','APROBADA'),
 ('d1000000-0000-0000-0000-000000000003','c1000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000003','A','REPROBADA');

INSERT INTO academic.grades (enrollment_subject_id, partial_number, grade_value) VALUES
 ('d1000000-0000-0000-0000-000000000001', 4, 8.5),
 ('d1000000-0000-0000-0000-000000000002', 4, 9.0),
 ('d1000000-0000-0000-0000-000000000003', 4, 5.0);

-- Inscripción periodo activo 2026-1 (cursando semestre 2)
INSERT INTO academic.enrollments (id, student_id, academic_period_id, status)
 SELECT 'c1000000-0000-0000-0000-000000000002', 'b1000000-0000-0000-0000-000000000001', p.id, 'ACTIVA'
 FROM academic.academic_periods p WHERE p.code='2026-1';

INSERT INTO academic.enrollment_subjects (id, enrollment_id, subject_id, group_code, status) VALUES
 ('d1000000-0000-0000-0000-000000000004','c1000000-0000-0000-0000-000000000002','a1000000-0000-0000-0000-000000000004','A','CURSANDO'),
 ('d1000000-0000-0000-0000-000000000005','c1000000-0000-0000-0000-000000000002','a1000000-0000-0000-0000-000000000006','A','CURSANDO');

-- ---------------------------------------------------------------------
-- STUDENT DOCUMENTS
-- ---------------------------------------------------------------------
INSERT INTO document.student_documents (student_id, document_type_id, file_name, file_path, mime_type, status)
 SELECT 'b1000000-0000-0000-0000-000000000001', dt.id, 'curp_jperez.pdf', '/storage/2025ISC0001/curp.pdf', 'application/pdf', 'VALIDADO'
 FROM document.document_types dt WHERE dt.code='CURP';
INSERT INTO document.student_documents (student_id, document_type_id, file_name, file_path, mime_type, status)
 SELECT 'b1000000-0000-0000-0000-000000000001', dt.id, 'acta_jperez.pdf', '/storage/2025ISC0001/acta.pdf', 'application/pdf', 'PENDIENTE'
 FROM document.document_types dt WHERE dt.code='ACTA_NAC';
INSERT INTO document.student_documents (student_id, document_type_id, file_name, file_path, mime_type, status, reviewed_by, rejection_reason)
 SELECT 'b1000000-0000-0000-0000-000000000001', dt.id, 'domicilio_jperez.pdf', '/storage/2025ISC0001/domicilio.pdf', 'application/pdf', 'RECHAZADO',
        '44444444-4444-4444-4444-444444444444', 'Comprobante con más de 3 meses de antigüedad'
 FROM document.document_types dt WHERE dt.code='COMP_DOM';

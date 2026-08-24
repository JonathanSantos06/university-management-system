-- =====================================================================
-- catalogs.sql - Catálogos base del sistema
-- =====================================================================

-- ROLES (diseñado para crecer: DOCENTE, CONTROL_ESCOLAR, DIRECTOR)
INSERT INTO auth.roles (id, name, description) VALUES
 (gen_random_uuid(), 'ADMIN', 'Administrador del sistema'),
 (gen_random_uuid(), 'ALUMNO', 'Alumno'),
 (gen_random_uuid(), 'PERSONAL_ADMINISTRATIVO', 'Personal administrativo'),
 (gen_random_uuid(), 'DOCENTE', 'Docente (reservado - futuro)'),
 (gen_random_uuid(), 'CONTROL_ESCOLAR', 'Control escolar (reservado - futuro)'),
 (gen_random_uuid(), 'DIRECTOR', 'Dirección (reservado - futuro)');

-- PERMISOS base
INSERT INTO auth.permissions (id, name, description) VALUES
 (gen_random_uuid(), 'STUDENT_READ', 'Consultar alumnos'),
 (gen_random_uuid(), 'STUDENT_WRITE', 'Crear/editar alumnos'),
 (gen_random_uuid(), 'CAREER_MANAGE', 'Gestionar carreras'),
 (gen_random_uuid(), 'SUBJECT_MANAGE', 'Gestionar materias'),
 (gen_random_uuid(), 'CURRICULUM_MANAGE', 'Gestionar retículas'),
 (gen_random_uuid(), 'PERIOD_MANAGE', 'Gestionar periodos académicos'),
 (gen_random_uuid(), 'ENROLLMENT_MANAGE', 'Gestionar inscripciones'),
 (gen_random_uuid(), 'GRADE_MANAGE', 'Gestionar calificaciones'),
 (gen_random_uuid(), 'DOCUMENT_VALIDATE', 'Validar/rechazar documentos'),
 (gen_random_uuid(), 'KARDEX_GENERATE', 'Generar kardex'),
 (gen_random_uuid(), 'USER_MANAGE', 'Gestionar usuarios y roles');

-- DOCUMENT TYPES
INSERT INTO document.document_types (id, name, code, is_required, description) VALUES
 (gen_random_uuid(), 'Acta de nacimiento', 'ACTA_NAC', TRUE, 'Acta de nacimiento certificada'),
 (gen_random_uuid(), 'CURP', 'CURP', TRUE, 'Clave Única de Registro de Población'),
 (gen_random_uuid(), 'Certificado de bachillerato', 'CERT_BACH', TRUE, 'Certificado total de bachillerato'),
 (gen_random_uuid(), 'Comprobante de domicilio', 'COMP_DOM', TRUE, 'Vigencia no mayor a 3 meses'),
 (gen_random_uuid(), 'Identificación oficial', 'ID_OFICIAL', TRUE, 'INE / Pasaporte / Cédula'),
 (gen_random_uuid(), 'Otros documentos', 'OTROS', FALSE, 'Documentos adicionales');

-- ACADEMIC PERIODS
INSERT INTO academic.academic_periods (id, name, code, start_date, end_date, is_active) VALUES
 (gen_random_uuid(), '2025-2', '2025-2', '2025-08-01', '2025-12-15', FALSE),
 (gen_random_uuid(), '2026-1', '2026-1', '2026-01-12', '2026-06-05', TRUE);

-- CAREERS
INSERT INTO academic.careers (id, name, code, description, total_semesters, is_active) VALUES
 (gen_random_uuid(), 'Ingeniería en Sistemas Computacionales', 'ISC', 'Carrera de ingeniería en sistemas', 9, TRUE),
 (gen_random_uuid(), 'Licenciatura en Administración', 'LAE', 'Carrera de administración de empresas', 8, TRUE);

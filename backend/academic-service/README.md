# academic-service

Carreras, materias, retículas, prerrequisitos, periodos, inscripciones, calificaciones y
kardex. Java 21 · Spring Boot 3.5.4 · Spring Data JPA · PostgreSQL 17 (esquema `academic`).

## Ejecutar localmente

```bash
cd backend/academic-service
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Seguridad

Igual que `student-service`: no emite tokens, solo valida los JWT de `auth-service` con la
misma clave HMAC compartida (`JWT_SECRET`).

## Flujo típico de prueba

```bash
TOKEN="<accessToken de un usuario ADMIN>"
BASE="http://localhost:8080/api"

# 1. Crear periodo académico y activarlo
curl -X POST $BASE/academic-periods -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"name":"2026-2","code":"2026-2","startDate":"2026-08-01","endDate":"2026-12-15"}'
curl -X PATCH "$BASE/academic-periods/{periodId}/activate" -H "Authorization: Bearer $TOKEN"

# 2. Crear carrera
curl -X POST $BASE/careers -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"name":"Ingeniería en Sistemas Computacionales","code":"ISC","totalSemesters":9}'

# 3. Crear materias
curl -X POST $BASE/subjects -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"name":"Programación I","code":"ISC-101","credits":8,"hoursTheory":3,"hoursPractice":2}'

# 4. Agregar materia a la retícula (semestre 1)
curl -X POST "$BASE/careers/{careerId}/curriculum" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"subjectId":"{subjectId}","semester":1,"mandatory":true}'

# 5. (opcional) Prerrequisito: Programación II requiere Programación I
curl -X POST "$BASE/subjects/{programacion2Id}/prerequisites" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"prerequisiteSubjectId":"{programacion1Id}"}'

# 6. Inscribir a un alumno (studentId viene de student-service)
curl -X POST $BASE/enrollments -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{
    "studentId": "{studentId}",
    "academicPeriodId": "{periodId}",
    "subjects": [{"subjectId": "{subjectId}", "groupCode": "A"}]
  }'
# -> 422 si al alumno le falta algún prerrequisito aprobado

# 7. Capturar calificación (parcial 4 = final, actualiza el estatus automáticamente)
curl -X POST "$BASE/enrollment-subjects/{enrollmentSubjectId}/grades" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"partialNumber": 4, "gradeValue": 8.5}'

# 8. Consultar kardex del alumno
curl "$BASE/students/{studentId}/kardex" -H "Authorization: Bearer $TOKEN"
```

## Reglas de negocio

- **Prerrequisitos**: al inscribir una materia, se valida que el alumno tenga aprobadas
  todas las materias marcadas como prerrequisito (histórico vía `enrollment_subjects.status = APROBADA`).
  Si falta alguna, responde `422 Unprocessable Entity` con el detalle de las materias faltantes.
- **Calificación aprobatoria**: configurable en `sgu.academic.passing-grade` (default `6.0`).
  Al capturar el parcial marcado como "final" (`sgu.academic.final-partial-number`, default `4`),
  el servicio actualiza automáticamente `enrollment_subjects.status` a `APROBADA` o `REPROBADA`.
- **Un solo periodo activo**: `PATCH /api/academic-periods/{id}/activate` desactiva cualquier
  otro periodo que estuviera activo.
- **Una inscripción por alumno y periodo**: `enrollments` tiene UNIQUE(student_id, academic_period_id).

## Limitación conocida (a resolver en iteración posterior)

`GET /api/students/{studentId}/enrollments` y `.../kardex` no validan que `studentId`
pertenezca al usuario autenticado cuando el rol es `ALUMNO` — el JWT solo trae el `userId`
de `auth-service`, no el `student.id` (que vive en `student-service`). Por ahora se confía
en que el frontend siempre pasa el `id` correcto (obtenido de `GET /api/students/me` en
`student-service`). Para cerrar esto se necesita una llamada síncrona a `student-service`
o un caché local de `userId -> studentId` alimentado por eventos.

## Diseño de datos

- `career_id`, `subject_id`, `academic_period_id` son FK **dentro** del propio esquema `academic`.
- `student_id` (en `enrollments`) y `recorded_by` (en `grades`) son referencias **lógicas**
  a `student.students.id` y `auth.users.id` respectivamente — sin FK física entre servicios.

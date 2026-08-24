# student-service

Expediente del alumno: datos personales, domicilios, contactos de emergencia y estatus
académico general. Java 21 · Spring Boot 3.5.4 · Spring Data JPA · PostgreSQL 17 (esquema `student`).

## Ejecutar localmente

Requiere PostgreSQL con `sql/schema.sql` + `sql/catalogs.sql` + `sql/data.sql` aplicados.

```bash
cd backend/student-service
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Seguridad

Este servicio **no emite tokens** — solo valida los JWT emitidos por `auth-service`,
usando la misma clave HMAC compartida (`sgu.jwt.secret`, variable `JWT_SECRET`). El filtro
(`JwtAuthenticationFilter`) reconstruye un `CurrentUser(userId, username, roles)` a partir
de los claims (`uid`, `sub`, `roles`) y lo expone como principal, disponible en los
controladores vía `@AuthenticationPrincipal CurrentUser currentUser`.

Roles esperados: `ADMIN`, `PERSONAL_ADMINISTRATIVO` (gestión de alumnos), `ALUMNO` (solo su
propio expediente vía `/api/students/me`).

## Endpoints

```bash
TOKEN="<accessToken obtenido en auth-service>"

# Alta de expediente (ADMIN / PERSONAL_ADMINISTRATIVO)
curl -X POST http://localhost:8080/api/students \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{
    "userId": "22222222-2222-2222-2222-222222222222",
    "studentCode": "2026ISC0099",
    "careerId": "<uuid de academic.careers>",
    "admissionPeriodId": "<uuid de academic.academic_periods>",
    "personalData": {
      "firstName": "Ana", "lastNamePaternal": "García", "lastNameMaternal": "Ruiz",
      "birthDate": "2006-05-10", "curp": "GARA060510MDFXXX01", "phone": "5511112222",
      "personalEmail": "ana.garcia@gmail.com"
    },
    "address": {
      "addressType": "ACTUAL", "street": "Calle 5", "city": "CDMX", "state": "CDMX", "postalCode": "01000"
    },
    "emergencyContact": {
      "fullName": "Luis García", "relationship": "Padre", "phone": "5533334444"
    }
  }'

# Búsqueda / listado paginado
curl "http://localhost:8080/api/students?query=perez&page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# Expediente propio (rol ALUMNO)
curl http://localhost:8080/api/students/me -H "Authorization: Bearer $TOKEN_ALUMNO"

# Cambiar estatus
curl -X PATCH http://localhost:8080/api/students/{id}/status \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"status":"BAJA_TEMPORAL"}'
```

## Notas de diseño

- `career_id` y `admission_period_id` son referencias **lógicas** a `academic-service`
  (sin FK física) — este servicio no valida que existan (se hará vía llamada REST a
  `academic-service` en una iteración posterior, o vía evento).
- `user_id` referencia lógicamente a `auth.users.id`; `/api/students/me` resuelve el
  expediente cruzando el `uid` del JWT contra `student.user_id`.
- Cascada `ALL` + `orphanRemoval` en `Student → personalData/addresses/emergencyContacts`:
  el expediente se trata como un agregado (DDD) cuya raíz es `Student`.

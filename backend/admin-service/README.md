# admin-service

Orquestación y agregación: dashboards y reportes transversales. **No es dueño de datos
propios** — no tiene base de datos ni JPA. Consulta a `student-service`, `academic-service`
y `document-service` vía HTTP síncrono (`RestClient`, Spring 6.1+) y agrega los resultados.

## Ejecutar localmente

Requiere que `student-service` y `academic-service` estén corriendo (puertos 8082 y 8083 por
defecto). `document-service` es opcional para esta entrega — si no está disponible, el
dashboard responde igual con ese KPI en `null` y una nota en `warnings`.

```bash
cd backend/admin-service
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Variables de entorno

| Variable | Default (local) | Default (docker) |
|---|---|---|
| `STUDENT_SERVICE_URL` | `http://localhost:8082` | `http://student-service:8080` |
| `ACADEMIC_SERVICE_URL` | `http://localhost:8083` | `http://academic-service:8080` |
| `DOCUMENT_SERVICE_URL` | `http://localhost:8084` | `http://document-service:8080` |
| `JWT_SECRET` | clave demo compartida | igual |

## Endpoints

```bash
TOKEN="<accessToken de un usuario ADMIN o PERSONAL_ADMINISTRATIVO>"

curl http://localhost:8080/api/admin/dashboard -H "Authorization: Bearer $TOKEN"
# {
#   "totalStudents": 2,
#   "activeCareers": 2,
#   "activeEnrollments": 1,
#   "pendingDocuments": null,
#   "warnings": ["No se pudo obtener documentos pendientes (document-service no está disponible todavía)"]
# }

curl http://localhost:8080/api/admin/documents/pending -H "Authorization: Bearer $TOKEN"
```

## Diseño

- **Sin base de datos**: `admin-service` es una capa fina de agregación, no un dueño de
  datos. Todo lo que expone se calcula en el momento consultando a los demás servicios.
- **Propagación del token**: el `Authorization: Bearer <token>` que llega a `admin-service`
  se reenvía tal cual a cada servicio downstream, que lo valida de forma independiente con
  la misma clave HMAC compartida — `admin-service` no necesita conocer roles/permisos de
  los otros dominios, solo pasa el token.
- **Degradación controlada**: cada llamada a un servicio downstream está envuelta en
  try/catch (`Optional.empty()` si falla). El dashboard nunca regresa `5xx` solo porque un
  servicio esté caído — regresa lo que sí pudo obtener, más una lista de `warnings`
  explicando qué faltó. Esto es intencional: es exactamente el caso de `document-service`
  en esta entrega (todavía no existe), y el mismo mecanismo protege producción si cualquier
  servicio tiene una caída temporal.
- **`DocumentServiceClient` ya define el contrato esperado** (`GET /api/documents?status=PENDIENTE`)
  aunque `document-service` no esté construido aún, para no tener que retocar `admin-service`
  cuando se construya — solo debe respetarse esa forma de respuesta.
- Se agregó `GET /api/enrollments/count?status=ACTIVA` en `academic-service` específicamente
  para dar soporte a este dashboard (conteo eficiente sin traer todas las inscripciones).

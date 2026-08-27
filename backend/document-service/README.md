# document-service

Expediente digital: catálogo de tipos de documento, carga/consulta/validación/rechazo de
documentos, y generación del Kardex académico en PDF. Java 21 · Spring Boot 3.5.4 ·
Spring Data JPA · PostgreSQL 17 (esquema `document`) · Apache PDFBox.

## Ejecutar localmente

```bash
cd backend/document-service
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

Los archivos se guardan en `sgu.storage.base-path` (default `./storage` local, `/app/storage`
en docker sobre un volumen nombrado para persistir entre recreaciones del contenedor).

## Endpoints

```bash
TOKEN="<accessToken>"
BASE="http://localhost:8080/api"

# Catálogo de tipos de documento
curl $BASE/document-types -H "Authorization: Bearer $TOKEN"

# Subir un documento (multipart)
curl -X POST "$BASE/students/{studentId}/documents?documentTypeId={documentTypeId}" \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@/ruta/local/curp.pdf"

# Listar documentos de un alumno
curl "$BASE/students/{studentId}/documents" -H "Authorization: Bearer $TOKEN"

# Descargar un documento
curl "$BASE/documents/{documentId}/download" -H "Authorization: Bearer $TOKEN" -o descarga.pdf

# Validar / rechazar (ADMIN, PERSONAL_ADMINISTRATIVO)
curl -X PATCH "$BASE/documents/{documentId}/validate" -H "Authorization: Bearer $TOKEN"
curl -X PATCH "$BASE/documents/{documentId}/reject" -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"reason":"Documento ilegible, favor de volver a escanear"}'

# Documentos pendientes en todo el sistema (usado por admin-service)
curl "$BASE/documents?status=PENDIENTE" -H "Authorization: Bearer $TOKEN"

# Kardex en PDF (agrega datos de academic-service)
curl "$BASE/students/{studentId}/kardex/pdf" -H "Authorization: Bearer $TOKEN" -o kardex.pdf
```

## Kardex en PDF — cómo funciona

`GET /api/students/{studentId}/kardex/pdf` NO tiene datos académicos propios: llama a
`academic-service` (`GET /api/students/{studentId}/kardex`, el mismo endpoint JSON que ya
existe) propagando el `Authorization: Bearer` recibido, y renderiza el resultado como PDF
con Apache PDFBox (tabla simple: periodo, materia, créditos, calificación, estatus). Si
`academic-service` no responde, regresa `422` explicando que no se pudo obtener el
historial — no se genera un PDF vacío o con datos inventados.

## Almacenamiento de archivos

- Tipos permitidos configurables en `sgu.storage.allowed-content-types` (default: PDF, PNG, JPEG).
- Tamaño máximo: `spring.servlet.multipart.max-file-size` (default 10MB).
- Estructura en disco: `{base-path}/{studentId}/{uuid}-{nombre-original-sanitizado}`.
- `FileStorageService` normaliza y valida que la ruta resuelta no escape del directorio
  base (protección básica contra path traversal).

## Limitación conocida (igual que en los demás servicios)

`GET /api/students/{studentId}/documents`, la subida y la descarga no validan que
`studentId` pertenezca al usuario `ALUMNO` autenticado — mismo motivo que en
`student-service`/`academic-service`: el JWT solo trae el `userId` de `auth-service`, no el
`student.id`. Pendiente de resolver con una llamada a `student-service` o un caché por eventos.

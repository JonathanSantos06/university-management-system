# auth-service

Autenticación, usuarios, roles y permisos del SGU. Java 21 · Spring Boot 3.5.4 · Spring Security · JWT + Refresh Token · Spring Data JPA · PostgreSQL 17.

## Ejecutar localmente

Requiere PostgreSQL corriendo con `sql/schema.sql` + `sql/catalogs.sql` + `sql/data.sql` ya aplicados
(ver `docker/docker-compose.yml`, o levanta solo el contenedor `postgres`).

```bash
cd backend/auth-service
mvn spring-boot:run
```

Por defecto conecta a `jdbc:postgresql://localhost:5432/sgu` (usuario `sgu_user` / password `sgu_pass`).
Con Docker Compose usa el perfil `docker` (ya configurado en el `docker-compose.yml` de la Entrega 1).

Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs

## Variables de entorno relevantes

| Variable | Default | Descripción |
|---|---|---|
| `JWT_SECRET` | clave demo Base64 en `application.yml` | **Cambiar en producción.** Clave HMAC para firmar access tokens |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/sgu` | Conexión a PostgreSQL |
| `SPRING_PROFILES_ACTIVE` | (ninguno) | Usar `docker` dentro de contenedores |

## Endpoints

### Autenticación (públicos)

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"jperez","password":"Password123!"}'

# Respuesta:
# {
#   "accessToken": "eyJhbGciOi...",
#   "refreshToken": "6xQ...opaco...",
#   "tokenType": "Bearer",
#   "expiresIn": 900,
#   "user": { "id": "...", "username": "jperez", "roles": ["ALUMNO"], ... }
# }

# Refresh (rota el refresh token: el usado queda revocado)
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<refresh-token-recibido>"}'

# Logout (revoca el refresh token indicado)
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<refresh-token-actual>"}'
```

### Usuarios y roles (requieren `Authorization: Bearer <accessToken>`, rol ADMIN)

```bash
TOKEN="<accessToken de un usuario admin>"

curl http://localhost:8080/api/users -H "Authorization: Bearer $TOKEN"

curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"username":"nalumno","email":"nalumno@alumnos.sgu.edu.mx","password":"Password123!","roles":["ALUMNO"]}'

curl -X PATCH "http://localhost:8080/api/users/{id}/roles" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"roles":["ALUMNO","DOCENTE"]}'

curl http://localhost:8080/api/roles -H "Authorization: Bearer $TOKEN"
```

## Usuarios demo (password `Password123!` para todos)

| username | rol |
|---|---|
| admin | ADMIN |
| jperez | ALUMNO |
| mlopez | ALUMNO |
| control1 | PERSONAL_ADMINISTRATIVO |

## Diseño de seguridad

- **Access token**: JWT stateless, HS256, expira en 15 min (`sgu.jwt.access-token-expiration-minutes`). Incluye `roles` como claim; el gateway/servicios downstream solo necesitan validar la firma, no consultan BD.
- **Refresh token**: valor aleatorio opaco (no es JWT) devuelto al cliente; en BD solo se guarda su **hash SHA-256** (`auth.refresh_tokens.token_hash`), nunca el valor en claro. Expira en 7 días (`sgu.jwt.refresh-token-expiration-days`).
- **Rotación**: cada `/api/auth/refresh` revoca el refresh token usado y emite uno nuevo — mitiga reuso de tokens robados.
- **Contraseñas**: BCrypt (`BCryptPasswordEncoder`, factor de costo por defecto).
- **Autorización**: `@PreAuthorize` a nivel de método con roles extraídos del JWT (`ROLE_ADMIN`, `ROLE_ALUMNO`, etc.). Los roles `DOCENTE`, `CONTROL_ESCOLAR`, `DIRECTOR` ya existen en el catálogo (`catalogs.sql`) para uso futuro sin cambios de esquema.

## Próximos pasos sugeridos (fuera de esta entrega)

- Rate limiting / bloqueo tras intentos fallidos de login.
- Endpoint de recuperación de contraseña (email).
- Publicación de eventos (usuario creado/roles cambiados) para que otros servicios sincronicen caché local si aplica.

# api-gateway

Puerta de entrada única del SGU. Spring Cloud Gateway (reactivo, WebFlux) — enrutamiento
hacia los 5 microservicios, CORS centralizado y una capa barata de defensa en profundidad
antes de reenviar tráfico.

## ⚠️ Antes de compilar

Este proyecto fija `spring-cloud.version=2025.0.0` como BOM compatible con
`spring-boot-starter-parent:3.5.4`. El sandbox donde se generó este código **no tiene
salida a Maven Central**, así que esa versión no pudo verificarse en caliente. Antes de
construir la imagen, confirma la versión correcta en la
[tabla de compatibilidad de Spring Cloud](https://spring.io/projects/spring-cloud#overview)
para Spring Boot 3.5.x y ajusta `<spring-cloud.version>` en `pom.xml` si hace falta.

## Ejecutar localmente

Requiere que los 5 microservicios estén corriendo en sus puertos por defecto
(auth 8081, student 8082, academic 8083, document 8084, admin 8085).

```bash
cd backend/api-gateway
mvn spring-boot:run
```

El gateway escucha en `http://localhost:8080`. A partir de ahí, todo se consume a través
de él, por ejemplo:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"jperez","password":"Password123!"}'

TOKEN="<accessToken recibido>"
curl http://localhost:8080/api/students/me -H "Authorization: Bearer $TOKEN"
curl http://localhost:8080/api/careers -H "Authorization: Bearer $TOKEN"
curl http://localhost:8080/api/students/{studentId}/kardex/pdf -H "Authorization: Bearer $TOKEN" -o kardex.pdf
```

## Diseño de rutas

`/api/students/**` es ambiguo por sí solo: **tres** servicios distintos atienden distintas
sub-rutas bajo ese prefijo (`student-service`, `academic-service` y `document-service`).
Por eso las rutas están declaradas con prioridad explícita (`order`, menor = se evalúa
primero) de la más específica a la más genérica:

| Orden | Patrón | Destino |
|---|---|---|
| 1 | `/api/students/*/documents/**` | document-service |
| 2 | `/api/students/*/kardex/pdf` | document-service |
| 3 | `/api/documents/**`, `/api/document-types/**` | document-service |
| 4 | `/api/students/*/enrollments` | academic-service |
| 5 | `/api/students/*/kardex` | academic-service |
| 6 | `/api/careers/**`, `/api/subjects/**`, `/api/academic-periods/**`, `/api/enrollments/**`, `/api/enrollment-subjects/**` | academic-service |
| 7 | `/api/admin/**` | admin-service |
| 8 | `/api/students/**` (todo lo demás: perfil, alta, documentos personales, etc.) | student-service |
| 9 | `/api/auth/**`, `/api/users/**`, `/api/roles/**` | auth-service |

El orden 5 (`/api/students/*/kardex`, sin `/**`) no choca con el orden 2
(`/api/students/*/kardex/pdf`) porque el predicado `Path` de Spring Cloud Gateway exige
coincidencia exacta de segmentos cuando no se usa `/**` — `kardex` y `kardex/pdf` tienen
distinto número de segmentos, así que nunca se traslapan.

## Qué SÍ y qué NO valida el gateway

- **SÍ** rechaza con `401` (sin reenviar la petición) cualquier ruta bajo `/api/**`
  (excepto `login`/`refresh`/`logout`) que no traiga `Authorization: Bearer <token>`.
  Es una verificación barata de **presencia**, no de validez.
- **NO** valida la firma ni la expiración del JWT — esa validación criptográfica sigue
  siendo responsabilidad exclusiva de cada microservicio (todos comparten la misma clave
  HMAC de `auth-service` y ya la aplican de forma independiente, ver sus propios README).
  Decisión deliberada: evita duplicar la dependencia JJWT y la clave secreta en el
  gateway solo para repetir una validación que los servicios ya hacen correctamente — el
  gateway se mantiene simple y el corte de tráfico obviamente no autenticado sigue siendo
  gratis.

## Otros componentes

- **CORS centralizado**: configurado una sola vez en `application.yml`
  (`spring.cloud.gateway.globalcors`), en vez de repetirlo en cada microservicio.
- **`X-Request-Id`**: si el cliente no lo envía, el gateway genera uno y lo agrega tanto a
  la petición reenviada como a la respuesta, para poder correlacionar logs entre los 5
  servicios ante un mismo request.
- **Actuator**: `GET /actuator/gateway/routes` expone las rutas activas (útil para depurar
  problemas de enrutamiento); `management.endpoint.gateway.access: read-only` evita que se
  puedan modificar rutas en caliente vía ese endpoint.

## Pendiente para una futura entrega

- Rate limiting (requiere Redis con `RequestRateLimiter` — no se incluyó para no añadir
  otra pieza de infraestructura a esta entrega).
- Agregación de la documentación Swagger de los 5 servicios en un solo `swagger-ui` (hoy
  cada microservicio expone el suyo en su propio puerto).

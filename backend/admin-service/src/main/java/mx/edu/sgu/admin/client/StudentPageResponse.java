package mx.edu.sgu.admin.client;

/** Vista mínima de la respuesta paginada de student-service (Spring Data Page). */
public record StudentPageResponse(long totalElements, int totalPages) {
}

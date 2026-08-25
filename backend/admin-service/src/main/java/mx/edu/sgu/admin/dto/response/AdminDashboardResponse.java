package mx.edu.sgu.admin.dto.response;

import java.util.List;

public record AdminDashboardResponse(
        Long totalStudents,
        Long activeCareers,
        Long activeEnrollments,
        Long pendingDocuments,
        List<String> warnings
) {
}

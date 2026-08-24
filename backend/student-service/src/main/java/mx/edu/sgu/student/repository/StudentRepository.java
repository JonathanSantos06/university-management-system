package mx.edu.sgu.student.repository;

import mx.edu.sgu.student.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByUserId(UUID userId);

    Optional<Student> findByStudentCode(String studentCode);

    boolean existsByStudentCode(String studentCode);

    @Query("""
            SELECT s FROM Student s
            JOIN s.personalData pd
            WHERE (:careerId IS NULL OR s.careerId = :careerId)
              AND (:status IS NULL OR s.status = :status)
              AND (:query IS NULL OR LOWER(pd.firstName || ' ' || pd.lastNamePaternal || ' ' || COALESCE(pd.lastNameMaternal, ''))
                   LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(s.studentCode) LIKE LOWER(CONCAT('%', :query, '%')))
            """)
    Page<Student> search(@Param("query") String query,
                          @Param("careerId") UUID careerId,
                          @Param("status") mx.edu.sgu.student.domain.StudentStatus status,
                          Pageable pageable);
}

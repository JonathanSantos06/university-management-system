package mx.edu.sgu.student.repository;

import mx.edu.sgu.student.domain.StudentPersonalData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentPersonalDataRepository extends JpaRepository<StudentPersonalData, UUID> {

    Optional<StudentPersonalData> findByStudentId(UUID studentId);

    boolean existsByCurp(String curp);
}

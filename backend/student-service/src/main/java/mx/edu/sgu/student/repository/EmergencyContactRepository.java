package mx.edu.sgu.student.repository;

import mx.edu.sgu.student.domain.EmergencyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {

    List<EmergencyContact> findByStudentId(UUID studentId);
}

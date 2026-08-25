package mx.edu.sgu.academic.repository;

import mx.edu.sgu.academic.domain.SubjectPrerequisite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubjectPrerequisiteRepository extends JpaRepository<SubjectPrerequisite, UUID> {

    List<SubjectPrerequisite> findBySubjectId(UUID subjectId);

    boolean existsBySubjectIdAndPrerequisiteSubjectId(UUID subjectId, UUID prerequisiteSubjectId);
}

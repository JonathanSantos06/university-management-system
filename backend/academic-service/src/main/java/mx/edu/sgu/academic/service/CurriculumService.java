package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.Career;
import mx.edu.sgu.academic.domain.CareerSubject;
import mx.edu.sgu.academic.domain.Subject;
import mx.edu.sgu.academic.domain.SubjectPrerequisite;
import mx.edu.sgu.academic.dto.request.AddCurriculumSubjectRequest;
import mx.edu.sgu.academic.dto.request.AddPrerequisiteRequest;
import mx.edu.sgu.academic.dto.response.CurriculumSubjectResponse;
import mx.edu.sgu.academic.exception.BusinessRuleException;
import mx.edu.sgu.academic.exception.DuplicateResourceException;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.CareerSubjectRepository;
import mx.edu.sgu.academic.repository.SubjectPrerequisiteRepository;
import mx.edu.sgu.academic.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CurriculumService {

    private final CareerSubjectRepository careerSubjectRepository;
    private final SubjectPrerequisiteRepository prerequisiteRepository;
    private final SubjectRepository subjectRepository;
    private final CareerService careerService;

    @Transactional(readOnly = true)
    public List<CurriculumSubjectResponse> getCurriculum(UUID careerId) {
        careerService.getOrThrow(careerId); // valida existencia de la carrera
        return careerSubjectRepository.findByCareerIdOrderBySemesterAsc(careerId).stream()
                .map(cs -> CurriculumSubjectResponse.from(cs, prerequisitesOf(cs.getSubject().getId())))
                .toList();
    }

    @Transactional
    public CurriculumSubjectResponse addSubjectToCurriculum(UUID careerId, AddCurriculumSubjectRequest request) {
        Career career = careerService.getOrThrow(careerId);
        Subject subject = getSubjectOrThrow(request.subjectId());

        if (careerSubjectRepository.existsByCareerIdAndSubjectId(careerId, request.subjectId())) {
            throw new DuplicateResourceException("La materia ya pertenece a la retícula de esta carrera");
        }
        if (request.semester() > career.getTotalSemesters()) {
            throw new BusinessRuleException("El semestre excede la duración de la carrera ("
                    + career.getTotalSemesters() + " semestres)");
        }

        CareerSubject careerSubject = CareerSubject.builder()
                .career(career)
                .subject(subject)
                .semester(request.semester())
                .mandatory(request.mandatory() == null || request.mandatory())
                .build();

        return CurriculumSubjectResponse.from(careerSubjectRepository.save(careerSubject),
                prerequisitesOf(subject.getId()));
    }

    @Transactional
    public void removeSubjectFromCurriculum(UUID careerId, UUID subjectId) {
        CareerSubject careerSubject = careerSubjectRepository.findByCareerIdAndSubjectId(careerId, subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("La materia no pertenece a la retícula de esta carrera"));
        careerSubjectRepository.delete(careerSubject);
    }

    @Transactional
    public void addPrerequisite(UUID subjectId, AddPrerequisiteRequest request) {
        if (subjectId.equals(request.prerequisiteSubjectId())) {
            throw new BusinessRuleException("Una materia no puede ser prerrequisito de sí misma");
        }
        Subject subject = getSubjectOrThrow(subjectId);
        Subject prerequisite = getSubjectOrThrow(request.prerequisiteSubjectId());

        if (prerequisiteRepository.existsBySubjectIdAndPrerequisiteSubjectId(subjectId, request.prerequisiteSubjectId())) {
            throw new DuplicateResourceException("El prerrequisito ya está registrado");
        }

        prerequisiteRepository.save(SubjectPrerequisite.builder()
                .subject(subject)
                .prerequisiteSubject(prerequisite)
                .build());
    }

    @Transactional(readOnly = true)
    public List<CurriculumSubjectResponse.PrerequisiteResponse> prerequisitesOf(UUID subjectId) {
        return prerequisiteRepository.findBySubjectId(subjectId).stream()
                .map(sp -> new CurriculumSubjectResponse.PrerequisiteResponse(
                        sp.getPrerequisiteSubject().getId(),
                        sp.getPrerequisiteSubject().getCode(),
                        sp.getPrerequisiteSubject().getName()))
                .toList();
    }

    private Subject getSubjectOrThrow(UUID id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + id));
    }
}

package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.Subject;
import mx.edu.sgu.academic.dto.request.SubjectRequest;
import mx.edu.sgu.academic.dto.response.SubjectResponse;
import mx.edu.sgu.academic.exception.DuplicateResourceException;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    @Transactional(readOnly = true)
    public List<SubjectResponse> findAll() {
        return subjectRepository.findAll().stream().map(SubjectResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SubjectResponse findById(UUID id) {
        return SubjectResponse.from(getOrThrow(id));
    }

    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Ya existe una materia con el código: " + request.code());
        }
        Subject subject = Subject.builder()
                .name(request.name())
                .code(request.code())
                .credits(request.credits())
                .hoursTheory(request.hoursTheory() == null ? 0 : request.hoursTheory())
                .hoursPractice(request.hoursPractice() == null ? 0 : request.hoursPractice())
                .active(true)
                .build();
        return SubjectResponse.from(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectResponse update(UUID id, SubjectRequest request) {
        Subject subject = getOrThrow(id);
        subject.setName(request.name());
        subject.setCredits(request.credits());
        subject.setHoursTheory(request.hoursTheory() == null ? subject.getHoursTheory() : request.hoursTheory());
        subject.setHoursPractice(request.hoursPractice() == null ? subject.getHoursPractice() : request.hoursPractice());
        return SubjectResponse.from(subjectRepository.save(subject));
    }

    @Transactional
    public SubjectResponse setActive(UUID id, boolean active) {
        Subject subject = getOrThrow(id);
        subject.setActive(active);
        return SubjectResponse.from(subjectRepository.save(subject));
    }

    Subject getOrThrow(UUID id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Materia no encontrada: " + id));
    }
}

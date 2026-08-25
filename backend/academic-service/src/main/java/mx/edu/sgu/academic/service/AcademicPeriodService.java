package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.AcademicPeriod;
import mx.edu.sgu.academic.dto.request.AcademicPeriodRequest;
import mx.edu.sgu.academic.dto.response.AcademicPeriodResponse;
import mx.edu.sgu.academic.exception.BusinessRuleException;
import mx.edu.sgu.academic.exception.DuplicateResourceException;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.AcademicPeriodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AcademicPeriodService {

    private final AcademicPeriodRepository academicPeriodRepository;

    @Transactional(readOnly = true)
    public List<AcademicPeriodResponse> findAll() {
        return academicPeriodRepository.findAll().stream().map(AcademicPeriodResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AcademicPeriodResponse findById(UUID id) {
        return AcademicPeriodResponse.from(getOrThrow(id));
    }

    @Transactional
    public AcademicPeriodResponse create(AcademicPeriodRequest request) {
        if (academicPeriodRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Ya existe un periodo con el código: " + request.code());
        }
        if (!request.endDate().isAfter(request.startDate())) {
            throw new BusinessRuleException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        AcademicPeriod period = AcademicPeriod.builder()
                .name(request.name())
                .code(request.code())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .active(false)
                .build();
        return AcademicPeriodResponse.from(academicPeriodRepository.save(period));
    }

    /** Activa el periodo indicado y desactiva cualquier otro previamente activo. */
    @Transactional
    public AcademicPeriodResponse activate(UUID id) {
        AcademicPeriod period = getOrThrow(id);
        academicPeriodRepository.findByActiveTrue().forEach(p -> {
            if (!p.getId().equals(id)) {
                p.setActive(false);
                academicPeriodRepository.save(p);
            }
        });
        period.setActive(true);
        return AcademicPeriodResponse.from(academicPeriodRepository.save(period));
    }

    AcademicPeriod getOrThrow(UUID id) {
        return academicPeriodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo académico no encontrado: " + id));
    }
}

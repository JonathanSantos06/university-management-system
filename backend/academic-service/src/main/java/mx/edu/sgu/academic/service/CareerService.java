package mx.edu.sgu.academic.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.academic.domain.Career;
import mx.edu.sgu.academic.dto.request.CareerRequest;
import mx.edu.sgu.academic.dto.response.CareerResponse;
import mx.edu.sgu.academic.exception.DuplicateResourceException;
import mx.edu.sgu.academic.exception.ResourceNotFoundException;
import mx.edu.sgu.academic.repository.CareerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CareerService {

    private final CareerRepository careerRepository;

    @Transactional(readOnly = true)
    public List<CareerResponse> findAll() {
        return careerRepository.findAll().stream().map(CareerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CareerResponse findById(UUID id) {
        return CareerResponse.from(getOrThrow(id));
    }

    @Transactional
    public CareerResponse create(CareerRequest request) {
        if (careerRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Ya existe una carrera con el código: " + request.code());
        }
        if (careerRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Ya existe una carrera con el nombre: " + request.name());
        }
        Career career = Career.builder()
                .name(request.name())
                .code(request.code())
                .description(request.description())
                .totalSemesters(request.totalSemesters())
                .active(true)
                .build();
        return CareerResponse.from(careerRepository.save(career));
    }

    @Transactional
    public CareerResponse update(UUID id, CareerRequest request) {
        Career career = getOrThrow(id);
        career.setName(request.name());
        career.setDescription(request.description());
        career.setTotalSemesters(request.totalSemesters());
        return CareerResponse.from(careerRepository.save(career));
    }

    @Transactional
    public CareerResponse setActive(UUID id, boolean active) {
        Career career = getOrThrow(id);
        career.setActive(active);
        return CareerResponse.from(careerRepository.save(career));
    }

    Career getOrThrow(UUID id) {
        return careerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada: " + id));
    }
}

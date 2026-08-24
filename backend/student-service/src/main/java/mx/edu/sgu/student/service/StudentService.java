package mx.edu.sgu.student.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.student.domain.*;
import mx.edu.sgu.student.dto.request.*;
import mx.edu.sgu.student.dto.response.StudentResponse;
import mx.edu.sgu.student.dto.response.StudentSummaryResponse;
import mx.edu.sgu.student.exception.DuplicateResourceException;
import mx.edu.sgu.student.exception.ResourceNotFoundException;
import mx.edu.sgu.student.repository.AddressRepository;
import mx.edu.sgu.student.repository.EmergencyContactRepository;
import mx.edu.sgu.student.repository.StudentPersonalDataRepository;
import mx.edu.sgu.student.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentPersonalDataRepository personalDataRepository;
    private final AddressRepository addressRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    @Transactional(readOnly = true)
    public Page<StudentSummaryResponse> search(String query, UUID careerId, StudentStatus status, Pageable pageable) {
        return studentRepository.search(blankToNull(query), careerId, status, pageable)
                .map(StudentSummaryResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(UUID id) {
        return StudentResponse.from(getStudentOrThrow(id));
    }

    @Transactional(readOnly = true)
    public StudentResponse findByUserId(UUID userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe expediente para el usuario: " + userId));
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse create(CreateStudentRequest request) {
        if (studentRepository.existsByStudentCode(request.studentCode())) {
            throw new DuplicateResourceException("La matrícula ya existe: " + request.studentCode());
        }
        if (personalDataRepository.existsByCurp(request.personalData().curp())) {
            throw new DuplicateResourceException("La CURP ya está registrada: " + request.personalData().curp());
        }
        studentRepository.findByUserId(request.userId()).ifPresent(s -> {
            throw new DuplicateResourceException("El usuario ya tiene un expediente de alumno asociado");
        });

        Student student = Student.builder()
                .userId(request.userId())
                .studentCode(request.studentCode())
                .careerId(request.careerId())
                .admissionPeriodId(request.admissionPeriodId())
                .currentSemester((short) 1)
                .status(StudentStatus.ACTIVO)
                .build();
        student = studentRepository.save(student);

        StudentPersonalData personalData = toPersonalDataEntity(request.personalData(), student);
        personalDataRepository.save(personalData);
        student.setPersonalData(personalData);

        if (request.address() != null) {
            Address address = toAddressEntity(request.address(), student);
            addressRepository.save(address);
            student.getAddresses().add(address);
        }
        if (request.emergencyContact() != null) {
            EmergencyContact contact = toEmergencyContactEntity(request.emergencyContact(), student);
            emergencyContactRepository.save(contact);
            student.getEmergencyContacts().add(contact);
        }

        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse updatePersonalData(UUID studentId, PersonalDataRequest request) {
        Student student = getStudentOrThrow(studentId);
        StudentPersonalData pd = personalDataRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("El alumno no tiene datos personales registrados"));

        if (!pd.getCurp().equals(request.curp()) && personalDataRepository.existsByCurp(request.curp())) {
            throw new DuplicateResourceException("La CURP ya está registrada: " + request.curp());
        }

        pd.setFirstName(request.firstName());
        pd.setLastNamePaternal(request.lastNamePaternal());
        pd.setLastNameMaternal(request.lastNameMaternal());
        pd.setBirthDate(request.birthDate());
        pd.setGender(request.gender());
        pd.setCurp(request.curp());
        pd.setRfc(request.rfc());
        pd.setPhone(request.phone());
        pd.setPersonalEmail(request.personalEmail());
        personalDataRepository.save(pd);

        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse addAddress(UUID studentId, AddressRequest request) {
        Student student = getStudentOrThrow(studentId);
        Address address = toAddressEntity(request, student);
        addressRepository.save(address);
        student.getAddresses().add(address);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse addEmergencyContact(UUID studentId, EmergencyContactRequest request) {
        Student student = getStudentOrThrow(studentId);
        EmergencyContact contact = toEmergencyContactEntity(request, student);
        emergencyContactRepository.save(contact);
        student.getEmergencyContacts().add(contact);
        return StudentResponse.from(student);
    }

    @Transactional
    public StudentResponse updateStatus(UUID studentId, StudentStatus status) {
        Student student = getStudentOrThrow(studentId);
        student.setStatus(status);
        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional
    public StudentResponse updateSemester(UUID studentId, short semester) {
        Student student = getStudentOrThrow(studentId);
        student.setCurrentSemester(semester);
        return StudentResponse.from(studentRepository.save(student));
    }

    private Student getStudentOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alumno no encontrado: " + id));
    }

    private StudentPersonalData toPersonalDataEntity(PersonalDataRequest r, Student student) {
        return StudentPersonalData.builder()
                .student(student)
                .firstName(r.firstName())
                .lastNamePaternal(r.lastNamePaternal())
                .lastNameMaternal(r.lastNameMaternal())
                .birthDate(r.birthDate())
                .gender(r.gender())
                .curp(r.curp())
                .rfc(r.rfc())
                .phone(r.phone())
                .personalEmail(r.personalEmail())
                .build();
    }

    private Address toAddressEntity(AddressRequest r, Student student) {
        return Address.builder()
                .student(student)
                .addressType(r.addressType())
                .street(r.street())
                .extNumber(r.extNumber())
                .intNumber(r.intNumber())
                .neighborhood(r.neighborhood())
                .city(r.city())
                .state(r.state())
                .postalCode(r.postalCode())
                .country(r.country() == null ? "MÉXICO" : r.country())
                .build();
    }

    private EmergencyContact toEmergencyContactEntity(EmergencyContactRequest r, Student student) {
        return EmergencyContact.builder()
                .student(student)
                .fullName(r.fullName())
                .relationship(r.relationship())
                .phone(r.phone())
                .email(r.email())
                .build();
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}

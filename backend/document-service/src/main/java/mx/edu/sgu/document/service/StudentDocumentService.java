package mx.edu.sgu.document.service;

import lombok.RequiredArgsConstructor;
import mx.edu.sgu.document.domain.DocumentType;
import mx.edu.sgu.document.domain.StudentDocument;
import mx.edu.sgu.document.dto.response.PendingDocumentResponse;
import mx.edu.sgu.document.dto.response.StudentDocumentResponse;
import mx.edu.sgu.document.exception.ResourceNotFoundException;
import mx.edu.sgu.document.repository.StudentDocumentRepository;
import mx.edu.sgu.document.security.CurrentUser;
import mx.edu.sgu.document.storage.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentDocumentService {

    private final StudentDocumentRepository studentDocumentRepository;
    private final DocumentTypeService documentTypeService;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<StudentDocumentResponse> findByStudent(UUID studentId) {
        return studentDocumentRepository.findByStudentIdOrderByUploadedAtDesc(studentId).stream()
                .map(StudentDocumentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PendingDocumentResponse> findPending() {
        return studentDocumentRepository.findByStatusOrderByUploadedAtAsc(StudentDocument.Status.PENDIENTE).stream()
                .map(PendingDocumentResponse::from)
                .toList();
    }

    @Transactional
    public StudentDocumentResponse upload(UUID studentId, UUID documentTypeId, MultipartFile file) {
        DocumentType documentType = documentTypeService.getOrThrow(documentTypeId);
        FileStorageService.StoredFile stored = fileStorageService.store(studentId, file);

        StudentDocument document = StudentDocument.builder()
                .studentId(studentId)
                .documentType(documentType)
                .fileName(file.getOriginalFilename())
                .filePath(stored.relativePath())
                .mimeType(stored.mimeType())
                .status(StudentDocument.Status.PENDIENTE)
                .build();

        return StudentDocumentResponse.from(studentDocumentRepository.save(document));
    }

    @Transactional
    public StudentDocumentResponse validate(UUID documentId, CurrentUser currentUser) {
        StudentDocument document = getOrThrow(documentId);
        document.setStatus(StudentDocument.Status.VALIDADO);
        document.setReviewedBy(currentUser == null ? null : currentUser.userId());
        document.setReviewedAt(OffsetDateTime.now());
        document.setRejectionReason(null);
        return StudentDocumentResponse.from(studentDocumentRepository.save(document));
    }

    @Transactional
    public StudentDocumentResponse reject(UUID documentId, String reason, CurrentUser currentUser) {
        StudentDocument document = getOrThrow(documentId);
        document.setStatus(StudentDocument.Status.RECHAZADO);
        document.setReviewedBy(currentUser == null ? null : currentUser.userId());
        document.setReviewedAt(OffsetDateTime.now());
        document.setRejectionReason(reason);
        return StudentDocumentResponse.from(studentDocumentRepository.save(document));
    }

    @Transactional(readOnly = true)
    public Resource download(UUID documentId) {
        StudentDocument document = getOrThrow(documentId);
        return fileStorageService.loadAsResource(document.getFilePath());
    }

    @Transactional(readOnly = true)
    public StudentDocument getOrThrow(UUID id) {
        return studentDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Documento no encontrado: " + id));
    }
}

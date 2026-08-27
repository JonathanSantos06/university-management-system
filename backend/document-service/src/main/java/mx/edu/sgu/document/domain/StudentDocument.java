package mx.edu.sgu.document.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_documents", schema = "document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Referencia lógica a student.students.id — sin FK física entre esquemas/servicios. */
    @Column(name = "student_id", nullable = false)
    private UUID studentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.PENDIENTE;

    @Column(name = "uploaded_at", nullable = false)
    @Builder.Default
    private OffsetDateTime uploadedAt = OffsetDateTime.now();

    /** Referencia lógica a auth.users.id (quién validó/rechazó). */
    @Column(name = "reviewed_by")
    private UUID reviewedBy;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @Column(name = "rejection_reason", length = 255)
    private String rejectionReason;

    public enum Status { PENDIENTE, VALIDADO, RECHAZADO }
}

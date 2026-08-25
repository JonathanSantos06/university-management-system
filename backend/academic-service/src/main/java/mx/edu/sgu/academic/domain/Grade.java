package mx.edu.sgu.academic.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "grades", schema = "academic",
        uniqueConstraints = @UniqueConstraint(columnNames = {"enrollment_subject_id", "partial_number"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_subject_id", nullable = false)
    private EnrollmentSubject enrollmentSubject;

    @Column(name = "partial_number", nullable = false)
    private short partialNumber;

    @Column(name = "grade_value", nullable = false, precision = 4, scale = 1)
    private BigDecimal gradeValue;

    /** Referencia lógica a auth.users.id (quién capturó la calificación). */
    @Column(name = "recorded_by")
    private UUID recordedBy;

    @Column(name = "recorded_at", nullable = false)
    @Builder.Default
    private OffsetDateTime recordedAt = OffsetDateTime.now();
}

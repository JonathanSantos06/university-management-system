package mx.edu.sgu.student.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_personal_data", schema = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPersonalData {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false, unique = true)
    private Student student;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name_paternal", nullable = false, length = 80)
    private String lastNamePaternal;

    @Column(name = "last_name_maternal", length = 80)
    private String lastNameMaternal;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(length = 20)
    private String gender;

    @Column(nullable = false, unique = true, length = 18)
    private String curp;

    @Column(length = 13)
    private String rfc;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String nationality = "MEXICANA";

    @Column(length = 20)
    private String phone;

    @Column(name = "personal_email", length = 150)
    private String personalEmail;

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }
}

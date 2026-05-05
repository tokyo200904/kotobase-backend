package kotobase_backend.modules.exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "exam_attempt_sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "section_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal sectionScore;

    @Column(name = "is_passed_section", nullable = false)
    private Boolean isPassedSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttempt examAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private ExamSection section;
}

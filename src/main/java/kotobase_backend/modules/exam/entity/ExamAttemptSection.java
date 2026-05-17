package kotobase_backend.modules.exam.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.StatusSection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusSection status = StatusSection.locked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttempt examAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private ExamSection section;
}

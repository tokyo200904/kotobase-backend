package kotobase_backend.modules.exam.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.SectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "exam_sections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(name = "section_type",nullable = false)
    private SectionType sectionType;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "min_passing_score", nullable = false)
    private int minPassingScore;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ExamAttemptSection> examAttemptSections;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<QuestionGroups> questionGroups;
}

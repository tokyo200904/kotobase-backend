package kotobase_backend.modules.exam.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total_questions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "max_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal maxScore;

    @Column(name = "duration_minutes",nullable = false)
    private int durationMinutes;

    @Column(name = "passing_score", nullable = false)
    private int passingScore;

    @Column(name = "is_published")
    private boolean isPublished;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "jlpt_level_id")
    private JlptLevel level;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamSection> sections;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamAttempt> examAttempts;
}

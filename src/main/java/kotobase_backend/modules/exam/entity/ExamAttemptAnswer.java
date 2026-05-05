package kotobase_backend.modules.exam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "exam_attempt_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamAttemptAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_correct_snapshot", nullable = false)
    private Boolean isCorrectSnapshot;

    @Column(name = "points_earned", nullable = false, precision = 5, scale = 2)
    private BigDecimal pointsEarned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_answer_id", nullable = false)
    private Answer selectedAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttempt examAttempt;

}

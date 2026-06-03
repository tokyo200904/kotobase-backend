package kotobase_backend.modules.progress.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "user_daily_activities", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "study_date"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "study_date", nullable = false)
    private LocalDate studyDate;

    @Builder.Default
    @Column(name = "total_reviewed")
    private int totalReviewed = 0;

    @Builder.Default
    @Column(name = "correct_answers")
    private int correctAnswers = 0;

    @Builder.Default
    @Column(name = "seconds_spent")
    private Integer secondsSpent = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
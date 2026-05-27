package kotobase_backend.modules.progress.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.enums.LearningStatus;
import kotobase_backend.modules.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_item_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "item_id", "item_type"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserItemProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", nullable = false)
    private Integer itemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;

    @Builder.Default
    @Column(name = "memory_level")
    private Integer memoryLevel = 1;

    @Column(name = "next_review_date", nullable = false)
    private LocalDateTime nextReviewDate;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LearningStatus status = LearningStatus.NEW;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
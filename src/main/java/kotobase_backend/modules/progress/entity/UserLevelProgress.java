package kotobase_backend.modules.progress.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_level_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private JlptLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;

    @Column(name = "highest_unlocked_station", nullable = false)
    private Integer highestUnlockedStation;
}
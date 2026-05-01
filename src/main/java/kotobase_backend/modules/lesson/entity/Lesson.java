package kotobase_backend.modules.lesson.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.topic.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title",length = 100,nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type")
    private TargetType lessonType;

    @Column(name = "lesson_order", nullable = false)
    private Integer lessonOrder;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JlptLevel level;

    @OneToMany(mappedBy = "lesson")
    private List<Topic> topics;
}

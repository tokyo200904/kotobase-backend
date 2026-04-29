package kotobase_backend.modules.JlptLevel.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.lesson.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "jlpt_levels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JlptLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private Level level;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @OneToMany(mappedBy = "level")
    private List<Lesson> lessons;
}


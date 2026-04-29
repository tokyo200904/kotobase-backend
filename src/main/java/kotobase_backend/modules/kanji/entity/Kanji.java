package kotobase_backend.modules.kanji.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "kanjis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kanji {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characters", nullable = false, length = 10)
    private String characters;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @Column(name = "stroke_count",nullable = false)
    private Integer strokeCount;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @Column(name = "han", nullable = false)
    private String han;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JlptLevel level;

    @OneToMany(mappedBy = "kanji")
    private List<KanjiReading> readings;
}


package kotobase_backend.modules.kanji.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kotobase_backend.comom.enums.KanjiType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kanji_readings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanjiReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reading",nullable = false)
    private String reading;

    @Enumerated(EnumType.STRING)
    @Column(name = "reading_type",nullable = false)
    private KanjiType kanjiType;

    @Column(name = "romaji",nullable = false)
    private String romaji;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "kanji_id")
    private Kanji kanji;
}

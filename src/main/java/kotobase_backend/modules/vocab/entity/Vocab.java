package kotobase_backend.modules.vocab.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.audio.entity.Audio;
import kotobase_backend.modules.example.entity.Example;
import kotobase_backend.modules.vocabularyTopic.entity.VocabularyTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "vocabularies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vocab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "word")
    private String word;

    @Column(name = "romaji")
    private String romaji;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "reading")
    private String reading;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JlptLevel level;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @OneToMany(mappedBy = "vocabulary")
    private List<VocabularyTopic> vocabularyTopics;

    @OneToMany(mappedBy = "vocabulary")
    private List<Example> examples;

}

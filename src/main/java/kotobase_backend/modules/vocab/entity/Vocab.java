package kotobase_backend.modules.vocab.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.exam.entity.Audio;
import kotobase_backend.modules.examples.entity.ExampleVocab;
import kotobase_backend.modules.topic.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JlptLevel level;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "vocabulary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExampleVocab> exampleVocabs;

}

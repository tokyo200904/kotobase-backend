package kotobase_backend.modules.examples.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.audio.entity.Audio;
import kotobase_backend.modules.vocab.entity.Vocab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vocabulary_examples")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleVocab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "vocabulary_id")
    private Vocab vocabulary;

}

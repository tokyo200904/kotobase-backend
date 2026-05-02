package kotobase_backend.modules.examples.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.audio.entity.Audio;
import kotobase_backend.modules.grammar.entity.Grammar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "grammar_examples")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleGrammar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content",nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "meaning", nullable = false, columnDefinition = "TEXT")
    private String meaning;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "grammar_id", nullable = false)
    private Grammar grammar;
}

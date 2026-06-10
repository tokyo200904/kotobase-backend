package kotobase_backend.modules.practice.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.grammar.entity.Grammar;
import lombok.*;

@Entity
@Table(name = "grammar_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrammarExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grammar_id", nullable = false)
    private Grammar grammar;

    @Lob
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Lob
    @Column(name = "broken_chunks", nullable = false, columnDefinition = "TEXT")
    private String brokenChunks;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String explanation;
}
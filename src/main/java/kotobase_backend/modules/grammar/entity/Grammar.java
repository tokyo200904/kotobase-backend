package kotobase_backend.modules.grammar.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.examples.entity.ExampleGrammar;
import kotobase_backend.modules.lesson.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "grammars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grammar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "structure", nullable = false, columnDefinition = "TEXT")
    private String structure;

    @Column(name = "meaning", nullable = false, columnDefinition = "TEXT")
    private String meaning;

    @Column(name = "usages", nullable = false, columnDefinition = "TEXT")
    private String usages;

    @Column(name = "note", nullable = false, columnDefinition = "TEXT")
    private String note;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium;

    @ManyToOne
    @JoinColumn(name = "level_id")
    private JlptLevel level;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @OneToMany(mappedBy = "grammar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExampleGrammar>  exampleGrammars;

}

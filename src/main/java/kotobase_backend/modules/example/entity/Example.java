package kotobase_backend.modules.example.entity;

import jakarta.persistence.*;
import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.modules.audio.entity.Audio;
import kotobase_backend.modules.vocab.entity.Vocab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "examples")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "meaning", nullable = false)
    private String meaning;

    @Column(name = "romaji", nullable = false)
    private String romaji;

    @ManyToOne
    @JoinColumn(name = "audio_id")
    private Audio audio;

    @ManyToOne
    @JoinColumn(name = "target_Id", insertable = false, updatable = false)
    private Vocab vocabulary;

}

package kotobase_backend.modules.vocab.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vocabularies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class vocab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "kanji", columnDefinition = "NVARCHAR(100)")
    private String kanji;

    @Column(name = "romaji")
    private String romaji;

    @Column(name = "meaning")
    private String meaning;

    @Column(name = "reading")
    private String reading;

    @Column(name = "example")
    private String example;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

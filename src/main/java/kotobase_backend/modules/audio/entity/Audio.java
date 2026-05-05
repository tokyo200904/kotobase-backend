package kotobase_backend.modules.audio.entity;


import jakarta.persistence.*;
import kotobase_backend.modules.exam.entity.Question;
import kotobase_backend.modules.exam.entity.QuestionGroups;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "audios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "audio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionGroups> questionGroups;

    @OneToMany(mappedBy = "audio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> question;
}

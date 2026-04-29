package kotobase_backend.modules.topic.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.vocabularyTopic.entity.VocabularyTopic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @OneToMany(mappedBy = "topic")
    private List<VocabularyTopic> vocabularyTopics;
}

package kotobase_backend.modules.vocabularyTopic.entity;

import jakarta.persistence.*;
import kotobase_backend.modules.topic.entity.Topic;
import kotobase_backend.modules.vocab.entity.Vocab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vocabulary_topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyTopic {

    @EmbeddedId
    private VocabularyTopicId id;

    @ManyToOne
    @MapsId("vocabularyId")
    @JoinColumn(name = "vocabulary_id")
    private Vocab vocabulary;

    @ManyToOne
    @MapsId("topicId")
    @JoinColumn(name = "topic_id")
    private Topic topic;
}

package kotobase_backend.modules.vocabularyTopic.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyTopicId {
    private Integer vocabularyId;
    private Integer topicId;
}


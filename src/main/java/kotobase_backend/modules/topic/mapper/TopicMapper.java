package kotobase_backend.modules.topic.mapper;

import kotobase_backend.modules.topic.dto.response.TopicReponse;
import kotobase_backend.modules.topic.entity.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicReponse topicToTopicReponse(Topic topic) {
        TopicReponse topicReponse = new TopicReponse();
        topicReponse.setId(topic.getId());
        topicReponse.setName(topic.getName());
        return topicReponse;
    }
}

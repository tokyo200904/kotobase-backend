package kotobase_backend.modules.topic.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicCompactResponse {
    private Integer id;
    private String name;
}
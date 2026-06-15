package kotobase_backend.modules.topic.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TopicAdminResponse {
    private Integer id;
    private String name;
    private Integer lessonId;
    private String lessonTitle;
    private String levelName;
}
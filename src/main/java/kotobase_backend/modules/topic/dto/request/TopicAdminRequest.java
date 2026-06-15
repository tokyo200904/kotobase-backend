package kotobase_backend.modules.topic.dto.request;

import lombok.Data;

@Data
public class TopicAdminRequest {
    private String name;
    private Integer lessonId;
}
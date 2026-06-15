package kotobase_backend.modules.lesson.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonCompactResponse {
    private Integer id;
    private String title;
}

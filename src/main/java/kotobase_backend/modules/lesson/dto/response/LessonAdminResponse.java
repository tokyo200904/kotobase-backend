package kotobase_backend.modules.lesson.dto.response;

import kotobase_backend.comom.enums.TargetType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonAdminResponse {
    private Integer id;
    private String title;
    private TargetType lessonType;
    private Integer lessonOrder;
    private Integer levelId;
    private String levelName;
}

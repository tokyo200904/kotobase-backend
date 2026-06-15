package kotobase_backend.modules.lesson.dto.request;

import kotobase_backend.comom.enums.TargetType;
import lombok.Data;

@Data
public class LessonAdminRequest {
    private String title;
    private TargetType lessonType;
    private Integer lessonOrder;
    private Integer levelId;
}

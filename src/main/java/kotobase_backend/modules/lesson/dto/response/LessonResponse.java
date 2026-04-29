package kotobase_backend.modules.lesson.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonResponse {
    private Integer id;
    private String title;
    private Integer lessonOrder;
}

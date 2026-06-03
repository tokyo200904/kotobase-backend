package kotobase_backend.modules.progress.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizQuestionResponse {
    private Integer progressId;
    private Integer targetItemId;
    private String questionText;
    private String itemType;
    private List<OptionDto> options;
    private Integer correctId;


@Data
@AllArgsConstructor
public static class OptionDto {
    private Integer id;
    private String text;
}
}
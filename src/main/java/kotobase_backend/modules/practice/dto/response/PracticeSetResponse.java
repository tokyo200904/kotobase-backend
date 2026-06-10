package kotobase_backend.modules.practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kotobase_backend.comom.enums.PracticeType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PracticeSetResponse {
    private Long id;
    private String title;
    private PracticeType practiceType;
    private String passageText;
    private List<PracticeQuestionResponse> questions;
}
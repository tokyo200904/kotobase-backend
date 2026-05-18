package kotobase_backend.modules.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamStartResponse {
    private Long attemptId;
    private boolean isResume;
    private Long activeSectionId;
    private long remainingTimeSeconds;
    private Map<Long, Long> savedAnswers;
}

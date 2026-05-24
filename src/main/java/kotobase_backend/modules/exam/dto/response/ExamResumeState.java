package kotobase_backend.modules.exam.dto.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExamResumeState {
    private Long attemptId;
    private Long sessionId;
    private String nameSession;
    private Long remainingTime;
    private Map<Long,Long> savedAnswers;
}

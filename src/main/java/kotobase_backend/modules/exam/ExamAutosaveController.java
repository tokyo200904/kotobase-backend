package kotobase_backend.modules.exam;

import kotobase_backend.modules.exam.dto.request.AnswerSubmitRequest;
import kotobase_backend.modules.exam.service.ExamAutosaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ExamAutosaveController {
    private final ExamAutosaveService examAutosaveService;

    @MessageMapping("/exam/autosave")
    public void handleAutosave(@Payload AnswerSubmitRequest payload) {
        examAutosaveService.addAnswer(payload.getAttemptId(), payload.getQuestionId(), payload.getSelectedAnswerId());
    }
}

package kotobase_backend.modules.exam.service.scoring;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoringQueueService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Async("scoringTaskExecutor")
    @Transactional
    public void calculateScoreBackground(Long attemptId){

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy lịch sử thi"));
    }
}

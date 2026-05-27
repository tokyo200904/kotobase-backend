package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.modules.exam.service.ExamAttemptAnswerService;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import kotobase_backend.modules.exam.service.ExamAutosaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ExamAutosaveServiceImpl implements ExamAutosaveService {

    private final ExamAttemptAnswerService examAttemptAnswerService;
    private final Map<Long, Map<Long, Long>> answerBuffer = new ConcurrentHashMap<>();

    @Override
    public void addAnswer(Long attemptId, Long questionId, Long answerId) {
        answerBuffer.computeIfAbsent(attemptId, k -> new ConcurrentHashMap<>())
                .put(questionId, answerId);
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void flushBufferToDatabase() {
        if (answerBuffer.isEmpty()) return;
        Map<Long, Map<Long, Long>> snapshot;
        synchronized (answerBuffer) {
            snapshot = new HashMap<>(answerBuffer);
            answerBuffer.clear();
        }
        for (Map.Entry<Long, Map<Long, Long>> entry : snapshot.entrySet()) {
            Long attemptId = entry.getKey();
            Map<Long, Long> answers = entry.getValue();
            examAttemptAnswerService.aotuSaveAnswers(attemptId, answers);
        }
    }

    @Override
    public void flushSingleAttempt(Long attemptId) {
        Map<Long, Long> userAnswers;
        synchronized (answerBuffer) {
            userAnswers = answerBuffer.remove(attemptId);
        }
        if (userAnswers != null && !userAnswers.isEmpty()) {
            examAttemptAnswerService.aotuSaveAnswers(attemptId, userAnswers);
        }
    }
}

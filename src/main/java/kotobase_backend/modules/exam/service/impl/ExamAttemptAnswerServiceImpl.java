package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.entity.Answer;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.entity.ExamAttemptAnswer;
import kotobase_backend.modules.exam.entity.Question;
import kotobase_backend.modules.exam.repository.AnswerRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptAnswerRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.QuestionRepository;
import kotobase_backend.modules.exam.service.ExamAttemptAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamAttemptAnswerServiceImpl implements ExamAttemptAnswerService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    @Override
    public void aotuSaveAnswers(Long attemptId, Map<Long, Long> answersRam) {

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay luot thi"));

        if (examAttempt.getStatus() != AttemptStatus.in_progress) {
            throw new ResourceNotFoundException("bài thi đã hoàn thành");
        }

        if (answersRam == null || answersRam.isEmpty()) {
            return;
        }
        List<Long> questionIds = new ArrayList<>(answersRam.keySet());
        List<ExamAttemptAnswer> examAttemptAnswers = examAttemptAnswerRepository
                .findByExamAttempt_IdAndQuestion_IdIn(attemptId, questionIds);

        Map<Long, ExamAttemptAnswer> examAttemptAnswerMap = examAttemptAnswers.stream()
                .collect(Collectors.toMap(ans -> ans.getQuestion().getId(), ans -> ans));

        List<ExamAttemptAnswer> answersToSave = new ArrayList<>();

        for (Map.Entry<Long, Long> entry : answersRam.entrySet()) {
            Long questionId = entry.getKey();
            Long selectedAnswerId = entry.getValue();

            ExamAttemptAnswer targetRecord;
            if (examAttemptAnswerMap.containsKey(questionId)) {
                targetRecord = examAttemptAnswerMap.get(questionId);
            } else {
                targetRecord = new ExamAttemptAnswer();
                targetRecord.setExamAttempt(examAttempt);
                Question question = questionRepository.getReferenceById(questionId);
                targetRecord.setQuestion(question);
            }
            if (selectedAnswerId != null) {
                Answer selectedAnswer = answerRepository.getReferenceById(selectedAnswerId);
                targetRecord.setSelectedAnswer(selectedAnswer);
            } else {
                targetRecord.setSelectedAnswer(null);
            }
            answersToSave.add(targetRecord);
        }
        examAttemptAnswerRepository.saveAll(answersToSave);
        System.out.println(" Đã lưu thành công " + answersToSave.size() + " đáp án cho Attempt " + attemptId);
    }
}

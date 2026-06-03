package kotobase_backend.modules.exam.service.scoring;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.response.ExamWebSocketPayload;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringExecutorService {
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    @Transactional
    public void doCalculateScore(Long attemptId){
        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy lịch sử thi"));

        Map<Long, BigDecimal> sectionScores = new HashMap<>();
        Exam exam = examAttempt.getExam();

        List<ExamAttemptAnswer> listAttemptAnswer = examAttemptAnswerRepository
                .findByExamAttempt_Id(attemptId);

        List<Long> questionIds = listAttemptAnswer.stream()
                .map(a -> a.getQuestion().getId())
                .toList();

        Map<Long, Question> questionMap = questionRepository
                .findAllWithGroupAndSection(questionIds)
                .stream()
                .collect(Collectors.toMap(Question::getId, q -> q));

        for (ExamAttemptAnswer userAnswers : listAttemptAnswer) {
            Question question = questionMap.get(userAnswers.getQuestion().getId());
            if (question == null) {continue;}

            Long sectionId = question.getQuestionGroup().getSection().getId();

            boolean isCorret = false;
            BigDecimal score = BigDecimal.ZERO;

            if (userAnswers.getSelectedAnswer() != null) {
                Answer answer = answerRepository.findById(userAnswers.getSelectedAnswer().getId()).orElse(null);
                if (answer != null && answer.getIsCorrect()) {
                    isCorret = true;
                    score = question.getPoint();
                }
            }
            userAnswers.setIsCorrectSnapshot(isCorret);
            userAnswers.setPointsEarned(score);
            BigDecimal point = sectionScores.getOrDefault(sectionId, BigDecimal.ZERO);
            sectionScores.put(sectionId, point.add(score));
        }
        examAttemptAnswerRepository.saveAll(listAttemptAnswer);

        List<ExamAttemptSection> atemptSecsionList = examAttemptSectionRepository.findByExamAttempt_IdWithSection(attemptId);
        BigDecimal totalScore = BigDecimal.ZERO;
        boolean isAllSectionsPassed = true;
        for (ExamAttemptSection attemptSection : atemptSecsionList) {
            Long sectionId = attemptSection.getSection().getId();
            ExamSection section = attemptSection.getSection();

            BigDecimal point = sectionScores.getOrDefault(sectionId, BigDecimal.ZERO);

            boolean isPassed = point.compareTo(new BigDecimal(section.getMinPassingScore())) >=0;
            if (!isPassed) {
                isAllSectionsPassed = false;
            }
            attemptSection.setSectionScore(point);
            attemptSection.setIsPassedSection(isPassed);
            totalScore = totalScore.add(point);
        }
        examAttemptSectionRepository.saveAll(atemptSecsionList);

        boolean isPassedExam = false;
        if(isAllSectionsPassed){
            isPassedExam = totalScore.compareTo(new BigDecimal(exam.getPassingScore())) >= 0;
        }

        examAttempt.setIsPassed(isPassedExam);
        examAttempt.setTotalScore(totalScore);
        examAttemptRepository.save(examAttempt);

        ExamWebSocketPayload payload = new ExamWebSocketPayload();
        payload.setAction("SCORE_READY");
        payload.setMessage("Đã chấm điểm xong!");
        messagingTemplate.convertAndSend("/topic/exam/" + attemptId, payload);
        log.info("Scoring Queue Chấm xong Attempt ID: {} | Tổng điểm: {}", attemptId, totalScore);
    }
}

package kotobase_backend.modules.exam.service.scoring;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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

        try {
            ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                    .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy lịch sử thi"));

            Map<Long, BigDecimal> sectionScores = new HashMap<>();
            Exam exam = examAttempt.getExam();

            List<ExamAttemptAnswer> listAttemptAnswer = examAttemptAnswerRepository
                    .findByExamAttempt_Id(examAttempt.getId());

            for (ExamAttemptAnswer userAnswers : listAttemptAnswer) {
                Question question = questionRepository.findById(userAnswers.getQuestion().getId()).orElseThrow(() -> new ResourceNotFoundException("không tìm thấy question"));
                Long sectionId = question.getQuestionGroup().getSection().getId();

                boolean isCorret = false;
                BigDecimal score = BigDecimal.ZERO;

                if (userAnswers.getSelectedAnswer() != null) {
                    Answer answer = answerRepository.findById(userAnswers.getSelectedAnswer().getId()).orElse(null);
                    if (answer != null && answer.isCorrect()) {
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

            List<ExamAttemptSection> atemptSecsionList = examAttemptSectionRepository.findByExamAttempt_Id(attemptId);
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

            log.info("Scoring Queue Chấm xong Attempt ID: {} | Tổng điểm: {}", attemptId, totalScore);
        }catch (Exception e){
            log.error("Scoring Queue Lỗi khi chấm Attempt ID: {}", attemptId, e);
        }
    }
}

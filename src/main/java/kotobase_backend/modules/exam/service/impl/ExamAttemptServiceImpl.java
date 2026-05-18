package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.comom.exceptions.CustomException.ForbiddenException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.mapper.ExamAttemptMapper;
import kotobase_backend.modules.exam.repository.*;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamSectionRepository examSectionRepository;
    private final ExamAttemptMapper examAttemptMapper;
    private final ExamRepository examRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final UserRepository userRepository;

    @Override
    public SectionResponse getSectionDetail(Long attemptId, Long sectionId, Integer currentUserId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu lượt thi"));

        if (!attempt.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền xem bài thi này.");
        }
        if (attempt.getStatus() == AttemptStatus.submitted) {
            throw new RuntimeException("Bài thi này đã được nộp");
        }
        if (attempt.getStatus() == AttemptStatus.abandoned) {
            throw new RuntimeException("Bài thi này đã bị hủy");
        }

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndSection_Id(attemptId,sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy dữ liệu phần thi của bai thi"));

        if(attemptSection.getStatus() == StatusSection.locked){
            throw new ForbiddenException("phan thi chưa được mở, hãy hoàn thành phần thi trước đó");
        }

        if(attemptSection.getStatus() == StatusSection.completed){
            throw new ForbiddenException("Phần thi này đã kết thúc không thể xem lại đề");
        }

        ExamSection examSection = examSectionRepository.getExamSectionBySectionId(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phần thi"));

        return examAttemptMapper.toExamSectionResponse(examSection);
    }

    @Transactional
    @Override
    public ExamStartResponse startOrResumeExam(Integer userId, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay bai thi"));
        if (!exam.getIsPublished())
            throw new ResourceNotFoundException("de thi chua duoc mo");

        Optional<ExamAttempt> examAttempt = examAttemptRepository
                .findByUser_IdAndExam_IdAndStatus(userId,examId,AttemptStatus.in_progress);
        if (examAttempt.isPresent()) {
            return resumeExam(examAttempt.get());
        }
        else {
            return newExam(userId,exam);
        }

    }

    private ExamStartResponse newExam(Integer userId, Exam exam) {
        LocalDateTime now = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim user"));
        ExamAttempt newExamAttempt = new ExamAttempt();
        newExamAttempt.setExam(exam);
        newExamAttempt.setUser(user);
        newExamAttempt.setStatus(AttemptStatus.in_progress);
        newExamAttempt.setStartedAt(now);
        newExamAttempt = examAttemptRepository.save(newExamAttempt);

        List<ExamSection> ExamSections = examSectionRepository.findByExam_IdOrderByDisplayOrderAsc(exam.getId());
        if (ExamSections.isEmpty())
            throw new ResourceNotFoundException("đề thi bị lỗi cấu trúc không có phần thi");

        ExamAttemptSection firstSection = null;

        for (int i=0;i<ExamSections.size();i++) {
            ExamSection examSections = ExamSections.get(i);
            ExamAttemptSection examAttemptSection = new ExamAttemptSection();
            examAttemptSection.setSection(examSections);
            examAttemptSection.setExamAttempt(newExamAttempt);

            if (i == 0) {
                examAttemptSection.setStartedAt(now);
                examAttemptSection.setStatus(StatusSection.in_progress);
                firstSection = examAttemptSectionRepository.save(examAttemptSection);
            } else {
                examAttemptSection.setStatus(StatusSection.locked);
                examAttemptSectionRepository.save(examAttemptSection);
            }
        }
            ExamStartResponse examStartResponse = new ExamStartResponse();
            examStartResponse.setAttemptId(newExamAttempt.getId());
            examStartResponse.setResume(false);
            examStartResponse.setActiveSectionId(firstSection.getSection().getId());

            int time = ExamSections.get(0).getDurationMinutes();
            examStartResponse.setRemainingTimeSeconds(time * 60L);
            examStartResponse.setSavedAnswers(new HashMap<>());

        return examStartResponse;
    }

    private ExamStartResponse resumeExam(ExamAttempt  examAttempt) {
        LocalDateTime now = LocalDateTime.now();

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndStatus(examAttempt.getId(), StatusSection.in_progress)
                .orElseThrow(() -> new ResourceNotFoundException("phan thi bi loi trang thai "));

        ExamSection examSection = examSectionRepository.findById(attemptSection.getSection().getId())
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay bai thi"));

        Long time1 = Duration.between(attemptSection.getStartedAt(), now).getSeconds();
        Long time2 = examSection.getDurationMinutes()*60L;
        Long time3 = time2 - time1;

        if (time3 <= 0){
            throw new RuntimeException("da het gio");
        }
        List<ExamAttemptAnswer> attemptAnswers = examAttemptAnswerRepository.findByExamAttempt_Id(examAttempt.getId());
        Map<Long ,Long> answers = attemptAnswers.stream()
                .filter(a -> a.getSelectedAnswer() != null)
                .collect(Collectors.toMap(
                        a -> a.getQuestion().getId(),
                        a -> a.getSelectedAnswer().getId()

                ));
        ExamStartResponse examStartResponse = new ExamStartResponse();
        examStartResponse.setAttemptId(examAttempt.getId());
        examStartResponse.setResume(true);
        examStartResponse.setActiveSectionId(attemptSection.getSection().getId());
        examStartResponse.setRemainingTimeSeconds(time3);
        examStartResponse.setSavedAnswers(answers);

        return examStartResponse;

    }
}

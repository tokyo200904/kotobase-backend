package kotobase_backend.modules.exam.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ExamReviewResponse {
    private Long attemptId;
    private List<SectionReviewDto> sections;

    @Data
    public static class SectionReviewDto {
        private Long sectionId;
        private String sectionName;
        private List<QuestionGroupReviewDto> questionGroups;
    }

    @Data
    public static class QuestionGroupReviewDto {
        private Long groupId;
        private String content;
        private String imageUrl;
        private String audioUrl;
        private List<QuestionReviewDto> questions;
    }
    @Data
    public static class QuestionReviewDto {
        private Long questionId;
        private String content;
        private String imageUrl;
        private String audioUrl;
        private List<AnswerReviewDto> answers;
        private Long userSelectedAnswerId;
        private Long correctAnswerId;
        private Boolean isCorrect;
        private String explanation;
    }
    @Data
    public static class AnswerReviewDto {
        private Long id;
        private String content;
    }
}

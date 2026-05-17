package kotobase_backend.modules.exam.mapper;

import kotobase_backend.modules.exam.dto.response.AnswerResponse;
import kotobase_backend.modules.exam.dto.response.QuestionGroupResponse;
import kotobase_backend.modules.exam.dto.response.QuestionResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.entity.Answer;
import kotobase_backend.modules.exam.entity.ExamSection;
import kotobase_backend.modules.exam.entity.Question;
import kotobase_backend.modules.exam.entity.QuestionGroups;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamAttemptMapper {

    public QuestionResponse toExamQuestionResponse(Question question) {

        List<AnswerResponse> aw = question.getAnswers() == null ? List.of() :
                question.getAnswers().stream()
                        .map(this::toAnswerResponse)
                        .toList();

        QuestionResponse q = new QuestionResponse();
        q.setId(question.getId());
        q.setContent(question.getContent());
        q.setPoints(question.getPoint());
        q.setDisplayOrder(question.getDisplayOrder());
        q.setAudioUrl(question.getAudio().getUrl());
        q.setImageUrl(question.getImage().getImageUrl());
        q.setAnswers(aw);
        return q;
    }

    public QuestionGroupResponse toExamSectionGroupResponse(QuestionGroups questionGroups) {

        List<QuestionResponse> q = questionGroups.getQuestions() == null ? List.of() :
                questionGroups.getQuestions().stream()
                        .map(this::toExamQuestionResponse)
                        .toList();

        QuestionGroupResponse qg = new QuestionGroupResponse();
        qg.setId(questionGroups.getId());
        qg.setContent(questionGroups.getContent());
        qg.setDisplayOrder(questionGroups.getDisplayOrder());
        qg.setGroupType(questionGroups.getGroupType());
        qg.setAudioUrl(questionGroups.getAudio().getUrl());
        qg.setImageUrl(questionGroups.getImage().getImageUrl());
        qg.setQuestions(q);
        return qg;
    }

    public SectionResponse toExamSectionResponse(ExamSection examSection) {

        List<QuestionGroupResponse> qg = examSection.getQuestionGroups() == null ? List.of() :
                examSection.getQuestionGroups().stream()
                        .map(this::toExamSectionGroupResponse)
                        .toList();

        SectionResponse sr = new SectionResponse();
        sr.setId(examSection.getId());
        sr.setSectionType(examSection.getSectionType());
        sr.setMinutes(examSection.getDurationMinutes());
        sr.setQuestionGroups(qg);
        return sr;
    }

    private AnswerResponse toAnswerResponse(Answer answer) {
        AnswerResponse a = new AnswerResponse();
        a.setId(answer.getId());
        a.setContent(answer.getContent());
        a.setDisplayOrder(answer.getDisplayOrder());
        return a;
    }
}

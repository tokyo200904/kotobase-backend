package kotobase_backend.modules.exam.mapper;

import kotobase_backend.modules.exam.dto.request.QuestionGroupAdminRequest;
import kotobase_backend.modules.exam.dto.response.QuestionGroupAdminResponse;
import kotobase_backend.modules.exam.entity.Answer;
import kotobase_backend.modules.exam.entity.Question;
import kotobase_backend.modules.exam.entity.QuestionGroups;
import kotobase_backend.modules.exam.repository.AudioRepository;
import kotobase_backend.modules.exam.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionGroupAdminMapper {

    private final AudioRepository audioRepository;
    private final ImageRepository imageRepository;

    public void updateEntityFromRequest(QuestionGroupAdminRequest request, QuestionGroups group) {
        group.setGroupType(request.getGroupType());
        group.setContent(request.getContent());
        group.setDisplayOrder(request.getDisplayOrder());

        group.setAudio(request.getAudioId() != null ? audioRepository.findById(request.getAudioId()).orElse(null) : null);
        group.setImage(request.getImageId() != null ? imageRepository.findById(request.getImageId()).orElse(null) : null);

        if (group.getQuestions() != null) {
            group.getQuestions().clear();
        } else {
            group.setQuestions(new ArrayList<>());
        }

        if (request.getQuestions() != null) {
            List<Question> newQuestions = request.getQuestions().stream().map(qReq -> {
                Question q = new Question();
                q.setContent(qReq.getContent());
                q.setPoint(qReq.getPoint());
                q.setDisplayOrder(qReq.getDisplayOrder());
                q.setAudio(qReq.getAudioId() != null ? audioRepository.findById(qReq.getAudioId()).orElse(null) : null);
                q.setImage(qReq.getImageId() != null ? imageRepository.findById(qReq.getImageId()).orElse(null) : null);

                q.setQuestionGroup(group);

                q.setAnswers(new ArrayList<>());
                if (qReq.getAnswers() != null) {
                    List<Answer> newAnswers = qReq.getAnswers().stream().map(aReq -> {
                        Answer a = new Answer();
                        a.setContent(aReq.getContent());
                        a.setIsCorrect(aReq.getIsCorrect());
                        a.setDisplayOrder(aReq.getDisplayOrder());
                        a.setExplanation(aReq.getExplanation());

                        a.setQuestion(q);
                        return a;
                    }).toList();
                    q.getAnswers().addAll(newAnswers);
                }
                return q;
            }).toList();

            group.getQuestions().addAll(newQuestions);
        }
    }

    public QuestionGroupAdminResponse toResponse(QuestionGroups group) {
        List<QuestionGroupAdminResponse.QuestionAdminResponse> questionResponses = group.getQuestions() == null ? List.of() :
                group.getQuestions().stream().map(q -> {
                    List<QuestionGroupAdminResponse.AnswerAdminResponse> answerResponses = q.getAnswers() == null ? List.of() :
                            q.getAnswers().stream().map(a -> QuestionGroupAdminResponse.AnswerAdminResponse.builder()
                                    .id(a.getId())
                                    .content(a.getContent())
                                    .isCorrect(a.getIsCorrect())
                                    .displayOrder(a.getDisplayOrder())
                                    .explanation(a.getExplanation())
                                    .build()
                            ).toList();

                    return QuestionGroupAdminResponse.QuestionAdminResponse.builder()
                            .id(q.getId())
                            .content(q.getContent())
                            .point(q.getPoint())
                            .displayOrder(q.getDisplayOrder())
                            .audioId(q.getAudio() != null ? (long) q.getAudio().getId() : null)
                            .audioUrl(q.getAudio() != null ? q.getAudio().getUrl() : null)
                            .imageId(q.getImage() != null ? q.getImage().getId() : null)
                            .imageUrl(q.getImage() != null ? q.getImage().getImageUrl() : null)
                            .answers(answerResponses)
                            .build();
                }).toList();

        return QuestionGroupAdminResponse.builder()
                .id(group.getId())
                .groupType(group.getGroupType())
                .content(group.getContent())
                .displayOrder(group.getDisplayOrder())
                .audioId(group.getAudio() != null ? (long) group.getAudio().getId() : null)
                .audioUrl(group.getAudio() != null ? group.getAudio().getUrl() : null)
                .imageId(group.getImage() != null ? group.getImage().getId() : null)
                .imageUrl(group.getImage() != null ? group.getImage().getImageUrl() : null)
                .questions(questionResponses)
                .build();
    }
}
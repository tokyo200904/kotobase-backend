package kotobase_backend.modules.vocab.mapper;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.examples.entity.ExampleVocab;
import kotobase_backend.modules.topic.entity.Topic;
import kotobase_backend.modules.topic.repository.TopicRepository;
import kotobase_backend.modules.vocab.dto.request.VocabAdminRequest;
import kotobase_backend.modules.vocab.dto.response.VocabAdminResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VocabAdminMapper {
    private final JlptLevelRepository levelRepository;
    private final TopicRepository topicRepository;


    public void updateEntityFromRequest(VocabAdminRequest request, Vocab vocab) {
        vocab.setWord(request.getWord());
        vocab.setRomaji(request.getRomaji());
        vocab.setMeaning(request.getMeaning());
        vocab.setReading(request.getReading());

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Level"));
        vocab.setLevel(level);

        if (request.getTopicId() != null) {
            Topic topic = topicRepository.findById(request.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Topic"));
            vocab.setTopic(topic);
        }

        if (vocab.getExampleVocabs() != null) {
            vocab.getExampleVocabs().clear();
        } else {
            vocab.setExampleVocabs(new ArrayList<>());
        }

        if (request.getExamples() != null) {
            List<ExampleVocab> newExamples = request.getExamples().stream().map(req -> {
                ExampleVocab e = new ExampleVocab();
                e.setContent(req.getContent());
                e.setMeaning(req.getMeaning());
                e.setDisplayOrder(req.getDisplayOrder());
                e.setVocabulary(vocab); // Trói con vào cha
                return e;
            }).toList();
            vocab.getExampleVocabs().addAll(newExamples);
        }
    }

    public VocabAdminResponse toResponse(Vocab vocab) {

        VocabAdminResponse.TopicSummaryResponse topicSummary = null;
        if (vocab.getTopic() != null) {
            topicSummary = VocabAdminResponse.TopicSummaryResponse.builder()
                    .id(vocab.getTopic().getId())
                    .name(vocab.getTopic().getName())
                    .lessonTitle(vocab.getTopic().getLesson() != null ? vocab.getTopic().getLesson().getTitle() : "")
                    .build();
        }

        List<VocabAdminResponse.ExampleVocabResponse> exampleResponses = vocab.getExampleVocabs() == null ? List.of() :
                vocab.getExampleVocabs().stream().map(e -> VocabAdminResponse.ExampleVocabResponse.builder()
                        .id(e.getId())
                        .content(e.getContent())
                        .meaning(e.getMeaning())
                        .displayOrder(e.getDisplayOrder())
                        .build()
                ).toList();

        return VocabAdminResponse.builder()
                .id(vocab.getId())
                .word(vocab.getWord())
                .romaji(vocab.getRomaji())
                .meaning(vocab.getMeaning())
                .reading(vocab.getReading())
                .levelId(vocab.getLevel().getId())
                .levelName(vocab.getLevel().getLevel().name())
                .topic(topicSummary)
                .examples(exampleResponses)
                .build();
    }
}
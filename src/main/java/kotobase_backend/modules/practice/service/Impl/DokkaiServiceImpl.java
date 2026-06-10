package kotobase_backend.modules.practice.service.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kotobase_backend.comom.enums.PracticeType;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.examples.entity.ExampleVocab;
import kotobase_backend.modules.examples.repository.VocabularyExampleRepository;
import kotobase_backend.modules.practice.dto.response.PracticeAnswerResponse;
import kotobase_backend.modules.practice.dto.response.PracticeQuestionResponse;
import kotobase_backend.modules.practice.dto.response.PracticeSetResponse;
import kotobase_backend.modules.practice.entity.GrammarExercise;
import kotobase_backend.modules.practice.repository.GrammarExerciseRepository;
import kotobase_backend.modules.practice.service.DokkaiService;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DokkaiServiceImpl implements DokkaiService {

    private final GrammarExerciseRepository grammarExerciseRepository;
    private final VocabRepository vocabularyRepository;
    private final VocabularyExampleRepository exampleRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<PracticeQuestionResponse> generateVocabDokkai(Integer topicId) {
        List<Vocab> targetVocabs = vocabularyRepository.findByVocabularyTopics_Topic_Id(topicId);
        List<PracticeQuestionResponse> dokkaiResponses = new ArrayList<>();

        for (Vocab correctVocab : targetVocabs) {
            List<ExampleVocab> examples = exampleRepository.findByVocabulary_Id(correctVocab.getId());
            if (examples.isEmpty()) {
                continue;
            }

            ExampleVocab example = examples.get(0);
            String originalSentence = example.getContent();
            String targetWord = correctVocab.getWord();
            String questionContent = originalSentence.replace(targetWord, " [___] ");

            if (questionContent.equals(originalSentence)) {
                questionContent = "Chọn từ vựng đúng dạng nguyên bản có xuất hiện trong câu: " + originalSentence;
            }

            List<Vocab> distractors = vocabularyRepository.findRandomDistractors(topicId, correctVocab.getId());

            List<PracticeAnswerResponse> answers = new ArrayList<>();

            answers.add(PracticeAnswerResponse.builder()
                    .content(correctVocab.getWord())
                    .isCorrect(true)
                    .build());

            for (Vocab distractor : distractors) {
                answers.add(PracticeAnswerResponse.builder()
                        .content(distractor.getWord())
                        .isCorrect(false)
                        .build());
            }

            Collections.shuffle(answers);

            PracticeQuestionResponse question = PracticeQuestionResponse.builder()
                    .content(questionContent)
                    .explanation("Dịch nghĩa câu: " + example.getMeaning())
                    .answers(answers)
                    .build();

            dokkaiResponses.add(question);
        }

        return dokkaiResponses;
    }

    @Override
    public List<PracticeQuestionResponse> generateGrammarDokkai(Integer grammarId) {
        List<GrammarExercise> exercises = grammarExerciseRepository.findByGrammar_Id(grammarId);
        List<PracticeQuestionResponse> quizResponses = new ArrayList<>();

        for (GrammarExercise exercise : exercises) {
            List<PracticeAnswerResponse> answers = new ArrayList<>();

            try {
                List<String> chunks = objectMapper.readValue(
                        exercise.getBrokenChunks(),
                        new TypeReference<List<String>>() {}
                );

                for (int i = 0; i < chunks.size(); i++) {
                    answers.add(PracticeAnswerResponse.builder()
                            .content(chunks.get(i))
                            .correctOrder(i + 1)
                            .build());
                }

            } catch (Exception e) {
                continue;
            }
            Collections.shuffle(answers);

            PracticeQuestionResponse question = PracticeQuestionResponse.builder()
                    .content(exercise.getQuestionText())
                    .explanation(exercise.getExplanation())
                    .answers(answers)
                    .build();
            quizResponses.add(question);
        }

        return quizResponses;
    }

}
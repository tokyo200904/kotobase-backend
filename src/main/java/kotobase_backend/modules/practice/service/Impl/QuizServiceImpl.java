package kotobase_backend.modules.practice.service.Impl;

import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.practice.dto.response.PracticeAnswerResponse;
import kotobase_backend.modules.practice.dto.response.PracticeQuestionResponse;
import kotobase_backend.modules.practice.service.QuizService;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final VocabRepository vocabRepository;
    private final KanjiRepository kanjiRepository;

    @Override
    public List<PracticeQuestionResponse> generateVocabQuiz(Integer topicId) {
        List<Vocab> targetVocabs = vocabRepository.findByVocabularyTopics_Topic_Id(topicId);
        List<PracticeQuestionResponse> quizResponses = new ArrayList<>();

        for (Vocab correctVocab : targetVocabs) {
            List<Vocab> distractors = vocabRepository.findRandom(topicId, correctVocab.getId());

            List<PracticeAnswerResponse> answers = new ArrayList<>();

            answers.add(PracticeAnswerResponse.builder()
                    .content(correctVocab.getMeaning())
                    .isCorrect(true)
                    .build());

            for (Vocab distractor : distractors) {
                answers.add(PracticeAnswerResponse.builder()
                        .content(distractor.getMeaning())
                        .isCorrect(false)
                        .build());
            }

            Collections.shuffle(answers);

            PracticeQuestionResponse question = PracticeQuestionResponse.builder()
                    .content("Nghĩa của từ [" + correctVocab.getReading() + "] là gì?")
                    .answers(answers)
                    .build();

            quizResponses.add(question);
        }

        return quizResponses;
    }

    @Override
    public List<PracticeQuestionResponse> generateKanjiQuiz(Integer levelId) {
        List<Kanji> targetKanjis = kanjiRepository.findByLevel_Id(levelId);
        List<PracticeQuestionResponse> quizResponses = new ArrayList<>();

        for (Kanji correctKanji : targetKanjis) {
            List<Kanji> distractors = kanjiRepository.findRandomDistractors(levelId, correctKanji.getId());

            List<PracticeAnswerResponse> answers = new ArrayList<>();

            answers.add(PracticeAnswerResponse.builder()
                    .content(correctKanji.getHan() + " (" + correctKanji.getMeaning() + ")")
                    .isCorrect(true)
                    .build());

            for (Kanji distractor : distractors) {
                answers.add(PracticeAnswerResponse.builder()
                        .content(distractor.getHan() + " (" + distractor.getMeaning() + ")")
                        .isCorrect(false)
                        .build());
            }

            Collections.shuffle(answers);

            PracticeQuestionResponse question = PracticeQuestionResponse.builder()
                    .content("Chữ Hán [" + correctKanji.getCharacters() + "] có âm Hán Việt và ý nghĩa là gì?")
                    .answers(answers)
                    .build();

            quizResponses.add(question);
        }

        return quizResponses;
    }
}


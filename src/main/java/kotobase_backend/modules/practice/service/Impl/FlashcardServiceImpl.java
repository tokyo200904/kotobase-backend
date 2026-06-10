package kotobase_backend.modules.practice.service.Impl;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.kanji.repository.KanjiRepository;
import kotobase_backend.modules.practice.dto.response.FlashcardKanjiResponse;
import kotobase_backend.modules.practice.dto.response.FlashcardVocabResponse;
import kotobase_backend.modules.practice.service.FlashcardService;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final KanjiRepository kanjiRepository;
    private final VocabRepository vocabRepository;

    @Override
    public List<FlashcardKanjiResponse> getFlashcardKanji(Level level) {
        List<Kanji> kanji = kanjiRepository.findByLevel(level);
        return kanji.stream()
                .map(k ->
                        new FlashcardKanjiResponse(
                                k.getId(),
                                k.getCharacters(),
                                k.getMeaning(),
                                k.getHan()
                        )).toList();
    }

    @Override
    public List<FlashcardVocabResponse> getFlashcardVocab(Integer topicId) {
        List<Vocab> vocab = vocabRepository.findByVocabularyTopics_Topic_Id(topicId);

        return vocab.stream()
                .map(v -> {
                    FlashcardVocabResponse dto = new FlashcardVocabResponse();
                    dto.setId(v.getId());
                    dto.setMeaning(v.getMeaning());
                    dto.setReading(v.getReading());
                    dto.setExamples(
                            v.getExampleVocabs().stream()
                                    .map(e -> new ExampleResponse(
                                            e.getId(),
                                            e.getContent(),
                                            e.getMeaning(),
                                            e.getDisplayOrder()
                                    )).toList()
                    );
                    return dto;
                }).toList();
    }
}

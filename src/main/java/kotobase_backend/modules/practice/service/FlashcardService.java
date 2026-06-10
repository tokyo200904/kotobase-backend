package kotobase_backend.modules.practice.service;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.practice.dto.response.FlashcardKanjiResponse;
import kotobase_backend.modules.practice.dto.response.FlashcardVocabResponse;

import java.util.List;

public interface FlashcardService {
    public List<FlashcardKanjiResponse> getFlashcardKanji(Level level);
    public List<FlashcardVocabResponse> getFlashcardVocab(Integer topicId);

}

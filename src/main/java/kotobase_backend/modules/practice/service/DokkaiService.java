package kotobase_backend.modules.practice.service;

import kotobase_backend.modules.practice.dto.response.PracticeQuestionResponse;
import kotobase_backend.modules.practice.dto.response.PracticeSetResponse;

import java.util.List;

public interface DokkaiService {
    public List<PracticeQuestionResponse> generateVocabDokkai(Integer topicId);
    public List<PracticeQuestionResponse> generateGrammarDokkai(Integer grammarId);
}

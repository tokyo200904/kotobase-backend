package kotobase_backend.modules.practice.service;

import kotobase_backend.modules.practice.dto.response.PracticeQuestionResponse;
import kotobase_backend.modules.practice.dto.response.PracticeSetResponse;

import java.util.List;

public interface QuizService {
    public List<PracticeQuestionResponse> generateVocabQuiz(Integer topicId);
    public List<PracticeQuestionResponse> generateKanjiQuiz(Integer levelId);
}

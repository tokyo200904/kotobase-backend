package kotobase_backend.modules.grammar.service;

import kotobase_backend.modules.grammar.dto.response.GrammarResponse;
import kotobase_backend.modules.grammar.dto.response.GrammarTitleResponse;

import java.util.List;

public interface GrammarService {

    public List<GrammarTitleResponse> findByLessonId(Integer lessonId);
}

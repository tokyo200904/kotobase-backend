package kotobase_backend.modules.grammar.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.grammar.dto.request.GrammarAdminRequest;
import kotobase_backend.modules.grammar.dto.response.GrammarAdminResponse;
import org.springframework.data.domain.Pageable;

public interface GrammarAdminService {
    PageResponse<GrammarAdminResponse> getAllGrammars(String search, Integer levelId, Integer lessonId, Pageable pageable);
    GrammarAdminResponse getGrammarById(Integer id);
    GrammarAdminResponse createGrammar(GrammarAdminRequest request);
    GrammarAdminResponse updateGrammar(Integer id, GrammarAdminRequest request);
    void deleteGrammar(Integer id);
}

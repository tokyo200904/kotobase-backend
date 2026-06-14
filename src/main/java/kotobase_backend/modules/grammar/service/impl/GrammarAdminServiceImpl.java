package kotobase_backend.modules.grammar.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.grammar.dto.request.GrammarAdminRequest;
import kotobase_backend.modules.grammar.dto.response.GrammarAdminResponse;
import kotobase_backend.modules.grammar.entity.Grammar;
import kotobase_backend.modules.grammar.mapper.GrammarAdminMapper;
import kotobase_backend.modules.grammar.repository.GrammarRepository;
import kotobase_backend.modules.grammar.service.GrammarAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GrammarAdminServiceImpl implements GrammarAdminService {

    private final GrammarRepository grammarRepository;
    private final GrammarAdminMapper grammarMapper;

    @Override
    public PageResponse<GrammarAdminResponse> getAllGrammars(String search, Integer levelId, Integer lessonId, Pageable pageable) {
        Page<Grammar> page = grammarRepository.adminSearchGrammars(search, levelId, lessonId, pageable);
        return PageResponse.of(page.map(grammarMapper::toResponse));
    }

    @Override
    public GrammarAdminResponse getGrammarById(Integer id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu trúc ngữ pháp"));
        return grammarMapper.toResponse(grammar);
    }

    @Override
    @Transactional
    public GrammarAdminResponse createGrammar(GrammarAdminRequest request) {
        Grammar grammar = new Grammar();
        grammarMapper.updateEntityFromRequest(request, grammar);
        Grammar saved = grammarRepository.save(grammar);
        return grammarMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GrammarAdminResponse updateGrammar(Integer id, GrammarAdminRequest request) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu trúc ngữ pháp"));
        grammarMapper.updateEntityFromRequest(request, grammar);
        Grammar updated = grammarRepository.save(grammar);
        return grammarMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteGrammar(Integer id) {
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu trúc ngữ pháp"));
        grammarRepository.delete(grammar);
    }
}
package kotobase_backend.modules.vocab.service.impl;

import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.vocab;
import kotobase_backend.modules.vocab.mapper.VocabMapper;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import kotobase_backend.modules.vocab.service.VocabService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VocabServiceImpl implements VocabService {
    private final VocabRepository vocabRepository;
    private final VocabMapper vocabMapper;
    @Override
    public Page<VocabResponse> getAllVocabs(VocabRequest request) {
        try {
            int limit = request.getLimit();
            int page = request.getPage() - 1;
            Pageable pageable = PageRequest.of(page, limit);
            Page<vocab> pageVocab = vocabRepository.findByKanjiContaining(request.getSearch(), pageable);
            return  pageVocab.map(vocabMapper::mapToVocab);
        }
        catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách từ vựng", e);
        }
    }
}

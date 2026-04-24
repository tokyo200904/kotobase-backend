package kotobase_backend.modules.vocab.service.impl;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
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
        if (request.getLimit() <= 0) {
            throw new IllegalArgumentException("limit phải lớn hơn 0");
        }
        if (request.getPage() <= 0) {
            throw new IllegalArgumentException("Page phải lớn hơn không 0");
        }
            int limit = request.getLimit();
            int page = request.getPage() - 1 ;
            Pageable pageable = PageRequest.of(page, limit);
            Page<Vocab> pageVocab = vocabRepository.findByKanjiContaining(request.getSearch(), pageable);
        return  pageVocab.map(vocabMapper::mapToVocab);
    }

    @Override
    public VocabResponse getVocabById(long id) {
        Vocab newvocab = vocabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vocab id " + id + " not found"));
        return vocabMapper.mapToVocab(newvocab);
    }
}

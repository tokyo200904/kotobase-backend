package kotobase_backend.modules.vocab.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.vocab.dto.request.VocabAdminRequest;
import kotobase_backend.modules.vocab.dto.response.VocabAdminResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.mapper.VocabAdminMapper;
import kotobase_backend.modules.vocab.repository.VocabRepository;
import kotobase_backend.modules.vocab.service.VocabAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VocabAdminServiceImpl implements VocabAdminService {
    private final VocabRepository vocabRepository;
    private final VocabAdminMapper vocabMapper;

    @Override
    public PageResponse<VocabAdminResponse> getAllVocabs(String search, Integer levelId, Integer topicId, Pageable pageable) {
        Page<Vocab> vocabPage = vocabRepository.adminSearchVocabs(search, levelId, topicId, pageable);
        return PageResponse.of(vocabPage.map(vocabMapper::toResponse));
    }

    @Override
    public VocabAdminResponse getVocabById(Integer id) {
        Vocab vocab = vocabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Từ vựng"));
        return vocabMapper.toResponse(vocab);
    }

    @Override
    @Transactional
    public VocabAdminResponse createVocab(VocabAdminRequest request) {
        Vocab vocab = new Vocab();
        vocabMapper.updateEntityFromRequest(request, vocab);
        Vocab savedVocab = vocabRepository.save(vocab);
        return vocabMapper.toResponse(savedVocab);
    }

    @Override
    @Transactional
    public VocabAdminResponse updateVocab(Integer id, VocabAdminRequest request) {
        Vocab vocab = vocabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Từ vựng"));
        vocabMapper.updateEntityFromRequest(request, vocab);
        Vocab updatedVocab = vocabRepository.save(vocab);
        return vocabMapper.toResponse(updatedVocab);
    }

    @Override
    @Transactional
    public void deleteVocab(Integer id) {
        Vocab vocab = vocabRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Từ vựng"));
        vocabRepository.delete(vocab);
    }
}
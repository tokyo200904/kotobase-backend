package kotobase_backend.modules.vocab.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.vocab.dto.request.VocabAdminRequest;
import kotobase_backend.modules.vocab.dto.response.VocabAdminResponse;
import kotobase_backend.modules.vocab.dto.response.VocabCompactResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VocabAdminService {
    PageResponse<VocabAdminResponse> getAllVocabs(String search, Integer levelId, Integer topicId, Pageable pageable);
    VocabAdminResponse getVocabById(Integer id);
    VocabAdminResponse createVocab(VocabAdminRequest request);
    VocabAdminResponse updateVocab(Integer id, VocabAdminRequest request);
    List<VocabCompactResponse> getCompactVocabsByLevel(Integer levelId);
    void deleteVocab(Integer id);
}

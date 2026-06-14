package kotobase_backend.modules.vocab.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.vocab.dto.request.VocabAdminRequest;
import kotobase_backend.modules.vocab.dto.response.VocabAdminResponse;
import org.springframework.data.domain.Pageable;

public interface VocabAdminService {
    PageResponse<VocabAdminResponse> getAllVocabs(String search, Integer levelId, Integer topicId, Pageable pageable);
    VocabAdminResponse getVocabById(Integer id);
    VocabAdminResponse createVocab(VocabAdminRequest request);
    VocabAdminResponse updateVocab(Integer id, VocabAdminRequest request);
    void deleteVocab(Integer id);
}

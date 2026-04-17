package kotobase_backend.modules.vocab.service;

import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VocabService {
    public Page<VocabResponse> getAllVocabs(VocabRequest request);
}

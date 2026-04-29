package kotobase_backend.modules.vocab.service;

import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface VocabService {
    public Page<VocabResponse> getAllVocabs(VocabRequest request);
    public VocabResponse getVocabById(Integer id);
}

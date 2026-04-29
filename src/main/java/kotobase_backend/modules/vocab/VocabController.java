package kotobase_backend.modules.vocab;

import jakarta.validation.Valid;
import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.PageVocabResponse;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.service.VocabService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/vocab")
@RequiredArgsConstructor
public class VocabController {

    private final VocabService vocabService;

    @GetMapping("/by_topic")
    public ResponseEntity<PageVocabResponse<VocabResponse>> getList(@Valid @ModelAttribute VocabRequest request) {
        return ResponseEntity.ok(vocabService.getAllVocabs(request));
    }

    @GetMapping("/detal/{id}")
    public ResponseEntity<VocabResponse> getVocabById(@PathVariable Integer id) {
        return ResponseEntity.ok(vocabService.getVocabById(id));
    }
}
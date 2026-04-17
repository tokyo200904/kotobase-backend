package kotobase_backend.modules.vocab;

import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
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

    @GetMapping
    public ResponseEntity<Page<VocabResponse>> getList(@ModelAttribute VocabRequest request) {
        return ResponseEntity.ok(vocabService.getAllVocabs(request));
    }
}
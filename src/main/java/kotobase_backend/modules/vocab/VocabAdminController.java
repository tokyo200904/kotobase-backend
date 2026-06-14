package kotobase_backend.modules.vocab;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.vocab.dto.request.VocabAdminRequest;
import kotobase_backend.modules.vocab.dto.response.VocabAdminResponse;
import kotobase_backend.modules.vocab.service.VocabAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/vocab")
@RequiredArgsConstructor
public class VocabAdminController {
    private final VocabAdminService vocabAdminService;


    @GetMapping
    public ResponseEntity<PageResponse<VocabAdminResponse>> getAllVocabs(@RequestParam(required = false) String search,
                                                                         @RequestParam(required = false) Integer levelId,
                                                                         @RequestParam(required = false) Integer topicId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(vocabAdminService.getAllVocabs(search, levelId, topicId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VocabAdminResponse> getVocabById(@PathVariable Integer id) {
        return ResponseEntity.ok(vocabAdminService.getVocabById(id));
    }

    @PostMapping
    public ResponseEntity<VocabAdminResponse> createVocab(@RequestBody VocabAdminRequest request) {
        return ResponseEntity.ok(vocabAdminService.createVocab(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VocabAdminResponse> updateVocab(@PathVariable Integer id,
                                                          @RequestBody VocabAdminRequest request) {
        return ResponseEntity.ok(vocabAdminService.updateVocab(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVocab(@PathVariable Integer id) {
        vocabAdminService.deleteVocab(id);
        return ResponseEntity.ok("Xóa từ vựng thành công!");
    }
}

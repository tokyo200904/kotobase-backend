package kotobase_backend.modules.vocab;

import jakarta.validation.Valid;
import kotobase_backend.modules.vocab.dto.request.VocabRequest;
import kotobase_backend.modules.vocab.dto.response.PageVocabResponse;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import kotobase_backend.modules.vocab.service.VocabService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/vocab")
@RequiredArgsConstructor
public class VocabController {

    private final VocabService vocabService;

    @GetMapping("/by_topic")
    public ResponseEntity<PageVocabResponse<VocabResponse>> getList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @Valid @ModelAttribute VocabRequest request) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(vocabService.getAllVocabs(request,userId));
    }

//    @GetMapping("/detal/{id}")
//    public ResponseEntity<VocabResponse> getVocabById(@PathVariable Integer id) {
//        return ResponseEntity.ok(vocabService.getVocabById(id));
//    }
}
package kotobase_backend.modules.practice;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.practice.dto.response.FlashcardKanjiResponse;
import kotobase_backend.modules.practice.dto.response.FlashcardVocabResponse;
import kotobase_backend.modules.practice.dto.response.PracticeQuestionResponse;
import kotobase_backend.modules.practice.dto.response.PracticeSetResponse;
import kotobase_backend.modules.practice.service.FlashcardService;
import kotobase_backend.modules.practice.service.DokkaiService;
import kotobase_backend.modules.practice.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/practice")
@RequiredArgsConstructor
public class PracticeController {

    private final DokkaiService practiceService;
    private final FlashcardService flashcardService;
    private final QuizService quizService;

    @GetMapping("/vocab/dokkai")
    public ResponseEntity<List<PracticeQuestionResponse>> getVocabDokkai(@RequestParam Integer topicId) {
        return ResponseEntity.ok(practiceService.generateVocabDokkai(topicId));
    }

    @GetMapping("/grammar/dokkai")
    public ResponseEntity<List<PracticeQuestionResponse>> getGrammarSortQuizzes(@RequestParam Integer grammarId) {
        return ResponseEntity.ok(practiceService.generateGrammarDokkai(grammarId));
    }

    @GetMapping("/flashcard/kanji")
    public ResponseEntity<List<FlashcardKanjiResponse>> getFlashcardKanji(@RequestParam Level level) {
        return ResponseEntity.ok(flashcardService.getFlashcardKanji(level));
    }

    @GetMapping("/flashcard/vocab")
    public ResponseEntity<List<FlashcardVocabResponse>> getFlashcardVocab(@RequestParam Integer topicId) {
        return ResponseEntity.ok(flashcardService.getFlashcardVocab(topicId));
    }

    @GetMapping("/quiz/kanji")
    public ResponseEntity<List<PracticeQuestionResponse>> getQuizKanji(@RequestParam Integer levelId) {
        return ResponseEntity.ok(quizService.generateKanjiQuiz(levelId));
    }

    @GetMapping("/quiz/vocab")
    public ResponseEntity<List<PracticeQuestionResponse>> getQuizVocab(@RequestParam Integer topicId) {
        return ResponseEntity.ok(quizService.generateVocabQuiz(topicId));
    }
 }
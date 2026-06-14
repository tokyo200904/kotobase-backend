package kotobase_backend.modules.grammar.mapper;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.examples.entity.ExampleGrammar;
import kotobase_backend.modules.grammar.dto.request.GrammarAdminRequest;
import kotobase_backend.modules.grammar.dto.response.GrammarAdminResponse;
import kotobase_backend.modules.grammar.entity.Grammar;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.practice.entity.GrammarExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class GrammarAdminMapper {
    private final JlptLevelRepository levelRepository;
    private final LessonRepository lessonRepository;


    public void updateEntityFromRequest(GrammarAdminRequest request, Grammar grammar) {
        grammar.setTitle(request.getTitle());
        grammar.setStructure(request.getStructure());
        grammar.setMeaning(request.getMeaning());
        grammar.setUsages(request.getUsages());
        grammar.setNote(request.getNote());

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Level"));
        grammar.setLevel(level);

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Lesson"));
        grammar.setLesson(lesson);

        if (grammar.getExampleGrammars() != null) {
            grammar.getExampleGrammars().clear();
        } else {
            grammar.setExampleGrammars(new ArrayList<>());
        }

        if (request.getExamples() != null) {
            List<ExampleGrammar> newExamples = request.getExamples().stream().map(req -> {
                ExampleGrammar e = new ExampleGrammar();
                e.setContent(req.getContent());
                e.setMeaning(req.getMeaning());
                e.setDisplayOrder(req.getDisplayOrder());
                e.setGrammar(grammar);
                return e;
            }).toList();
            grammar.getExampleGrammars().addAll(newExamples);
        }

        if (grammar.getExercises() != null) {
            grammar.getExercises().clear();
        } else {
            grammar.setExercises(new ArrayList<>());
        }

        if (request.getExercises() != null) {
            List<GrammarExercise> newExercises = request.getExercises().stream().map(req -> {
                GrammarExercise ex = new GrammarExercise();
                ex.setQuestionText(req.getQuestionText());
                ex.setBrokenChunks(req.getBrokenChunks());
                ex.setExplanation(req.getExplanation());
                ex.setGrammar(grammar);
                return ex;
            }).toList();
            grammar.getExercises().addAll(newExercises);
        }
    }

    public GrammarAdminResponse toResponse(Grammar grammar) {
        List<GrammarAdminResponse.ExampleGrammarResponse> exampleResponses = grammar.getExampleGrammars() == null ? List.of() :
                grammar.getExampleGrammars().stream().map(e -> GrammarAdminResponse.ExampleGrammarResponse.builder()
                        .id(e.getId()).content(e.getContent()).meaning(e.getMeaning()).displayOrder(e.getDisplayOrder()).build()
                ).toList();

        List<GrammarAdminResponse.GrammarExerciseResponse> exerciseResponses = grammar.getExercises() == null ? List.of() :
                grammar.getExercises().stream().map(ex -> GrammarAdminResponse.GrammarExerciseResponse.builder()
                        .id(ex.getId()).questionText(ex.getQuestionText()).brokenChunks(ex.getBrokenChunks()).explanation(ex.getExplanation()).build()
                ).toList();

        return GrammarAdminResponse.builder()
                .id(grammar.getId())
                .title(grammar.getTitle())
                .structure(grammar.getStructure())
                .meaning(grammar.getMeaning())
                .usages(grammar.getUsages())
                .note(grammar.getNote())
                .levelId(grammar.getLevel().getId())
                .levelName(grammar.getLevel().getLevel().name())
                .lessonId(grammar.getLesson().getId())
                .lessonTitle(grammar.getLesson().getTitle())
                .examples(exampleResponses)
                .exercises(exerciseResponses)
                .build();
    }
}
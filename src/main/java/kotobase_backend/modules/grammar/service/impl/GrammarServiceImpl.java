package kotobase_backend.modules.grammar.service.impl;

import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.comom.exceptions.CustomException.BadRequestException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.grammar.dto.response.GrammarResponse;
import kotobase_backend.modules.grammar.dto.response.GrammarTitleResponse;
import kotobase_backend.modules.grammar.entity.Grammar;
import kotobase_backend.modules.grammar.mapper.GrammarMapper;
import kotobase_backend.modules.grammar.repository.GrammarRepository;
import kotobase_backend.modules.grammar.service.GrammarService;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrammarServiceImpl implements GrammarService {

    private final GrammarRepository grammarRepository;
    private final LessonRepository lessonRepository;
    private final GrammarMapper grammarMapper;

    @Override
    public List<GrammarTitleResponse> findByLessonId(Integer lessonId) {

        if (lessonId == null) {
            throw new BadRequestException("lessonId không được  để trống");
        }
        lessonRepository.findByIdAndLessonType(lessonId, TargetType.grammar)
                .orElseThrow(() -> new ResourceNotFoundException("lesson của grammar này không tồn tại"));

        List<Grammar> grammar = grammarRepository.findAllByLessonId(lessonId);

        return grammar.stream()
                .map(grammarMapper::toGrammarTitleResponse)
                .toList();
    }

    @Override
    public GrammarResponse findById(Integer id) {
        if (id == null) {
            throw new BadRequestException("id không được null");
        }
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ngữ pháp này không tồn tại"));

        return grammarMapper.toGrammarResponse(grammar);
    }
}

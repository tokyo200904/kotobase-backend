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
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.payment.service.PremiumGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GrammarServiceImpl implements GrammarService {

    private final GrammarRepository grammarRepository;
    private final LessonRepository lessonRepository;
    private final GrammarMapper grammarMapper;
    private final PremiumGuardService premiumGuardService;

    @Override
    public List<GrammarTitleResponse> findByLessonId(Integer lessonId, Integer userId) {
        if (lessonId == null) {
            throw new BadRequestException("lessonId không được để trống");
        }

        Lesson lesson = lessonRepository.findByIdAndLessonType(lessonId, TargetType.grammar)
                .orElseThrow(() -> new ResourceNotFoundException("lesson của grammar này không tồn tại"));

        if (lesson.getLessonOrder() > 5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để xem ngữ pháp bài này.");
            }
            premiumGuardService.enforcePremium(userId, "Ngữ pháp bài học này chỉ dành cho tài khoản Premium.");
        }

        List<Grammar> grammar = grammarRepository.findAllByLessonId(lessonId);

        return grammar.stream()
                .map(grammarMapper::toGrammarTitleResponse)
                .toList();
    }

    @Override
    public GrammarResponse findById(Integer id, Integer userId) {
        if (id == null) {
            throw new BadRequestException("id không được null");
        }
        Grammar grammar = grammarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ngữ pháp này không tồn tại"));

        Lesson parentLesson = grammar.getLesson();
        if (parentLesson != null && parentLesson.getLessonOrder() > 5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để xem chi tiết ngữ pháp.");
            }
            premiumGuardService.enforcePremium(userId, "Nội dung này đã bị khóa. Vui lòng nâng cấp Premium.");
        }

        return grammarMapper.toGrammarResponse(grammar);
    }
}
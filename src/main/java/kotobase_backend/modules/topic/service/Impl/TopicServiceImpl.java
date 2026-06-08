package kotobase_backend.modules.topic.service.Impl;

import kotobase_backend.comom.exceptions.CustomException.BadRequestException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.payment.service.PremiumGuardService;
import kotobase_backend.modules.topic.dto.response.TopicReponse;
import kotobase_backend.modules.topic.entity.Topic;
import kotobase_backend.modules.topic.mapper.TopicMapper;
import kotobase_backend.modules.topic.repository.TopicRepository;
import kotobase_backend.modules.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final TopicMapper topicMapper;
    private final PremiumGuardService premiumGuardService;

    @Override
    public List<TopicReponse> findByLessonId(Integer lessonId, Integer userId) {
        if (lessonId == null) {
            throw new BadRequestException("khong tim thay lessonId");
        }
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson không tồn tại"));

        if (lesson.getLessonOrder() > 5) {
            if (userId == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vui lòng đăng nhập để xem nội dung bài này.");
            }
            premiumGuardService.enforcePremium(userId, "Nội dung bài học này chỉ dành cho tài khoản Premium.");
        }
        List<Topic> topic = topicRepository.findByLessonId(lessonId);
        return topic.stream()
                .map(topicMapper::topicToTopicReponse)
                .toList();
    }
}

package kotobase_backend.modules.topic.service.Impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.topic.dto.request.TopicAdminRequest;
import kotobase_backend.modules.topic.dto.response.TopicAdminResponse;
import kotobase_backend.modules.topic.dto.response.TopicCompactResponse;
import kotobase_backend.modules.topic.entity.Topic;
import kotobase_backend.modules.topic.mapper.TopicAdminMapper;
import kotobase_backend.modules.topic.repository.TopicRepository;
import kotobase_backend.modules.topic.service.TopicAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicAdminServiceImpl implements TopicAdminService {

    private final TopicRepository topicRepository;
    private final TopicAdminMapper topicMapper;

    @Override
    public PageResponse<TopicAdminResponse> getAllTopics(String search, Integer lessonId, Pageable pageable) {
        Page<Topic> page = topicRepository.adminSearchTopics(search, lessonId, pageable);
        return PageResponse.of(page.map(topicMapper::toResponse));
    }

    @Override
    public List<TopicCompactResponse> getCompactTopicsByLesson(Integer lessonId) {
        return topicRepository.findByLesson_IdOrderByIdAsc(lessonId)
                .stream().map(t -> TopicCompactResponse.builder()
                        .id(t.getId()).name(t.getName()).build()).toList();
    }

    @Override
    public TopicAdminResponse getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chủ đề"));
        return topicMapper.toResponse(topic);
    }

    @Override
    @Transactional
    public TopicAdminResponse createTopic(TopicAdminRequest request) {
        Topic topic = new Topic();
        topicMapper.updateEntityFromRequest(request, topic);
        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public TopicAdminResponse updateTopic(Integer id, TopicAdminRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chủ đề"));
        topicMapper.updateEntityFromRequest(request, topic);
        return topicMapper.toResponse(topicRepository.save(topic));
    }

    @Override
    @Transactional
    public void deleteTopic(Integer id) {
        topicRepository.deleteById(id);
    }
}
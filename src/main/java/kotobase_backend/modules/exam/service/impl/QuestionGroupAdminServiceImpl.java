package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.request.QuestionGroupAdminRequest;
import kotobase_backend.modules.exam.dto.response.QuestionGroupAdminResponse;
import kotobase_backend.modules.exam.entity.ExamSection;
import kotobase_backend.modules.exam.entity.QuestionGroups;
import kotobase_backend.modules.exam.mapper.QuestionGroupAdminMapper;
import kotobase_backend.modules.exam.repository.ExamSectionRepository;
import kotobase_backend.modules.exam.repository.QuestionGroupRepository;
import kotobase_backend.modules.exam.service.QuestionGroupAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionGroupAdminServiceImpl implements QuestionGroupAdminService {
    private final QuestionGroupRepository groupRepository;
    private final ExamSectionRepository sectionRepository;
    private final QuestionGroupAdminMapper groupMapper;

    @Override
    public List<QuestionGroupAdminResponse> getGroupsBySectionId(Long sectionId) {
        return groupRepository.findBySection_IdOrderByDisplayOrderAsc(sectionId)
                .stream().map(groupMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public QuestionGroupAdminResponse createGroup(Long sectionId, QuestionGroupAdminRequest request) {
        ExamSection section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Phần thi"));

        QuestionGroups group = new QuestionGroups();
        group.setSection(section);
        groupMapper.updateEntityFromRequest(request, group);

        QuestionGroups saved = groupRepository.save(group);
        return groupMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuestionGroupAdminResponse updateGroup(Long groupId, QuestionGroupAdminRequest request) {
        QuestionGroups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhóm câu hỏi"));

        groupMapper.updateEntityFromRequest(request, group);
        QuestionGroups updated = groupRepository.save(group);
        return groupMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteGroup(Long groupId) {
        QuestionGroups group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Nhóm câu hỏi"));
        groupRepository.delete(group);
    }
}
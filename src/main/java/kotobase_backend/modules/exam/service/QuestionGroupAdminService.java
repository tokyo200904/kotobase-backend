package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.request.QuestionGroupAdminRequest;
import kotobase_backend.modules.exam.dto.response.QuestionGroupAdminResponse;

import java.util.List;

public interface QuestionGroupAdminService {
    List<QuestionGroupAdminResponse> getGroupsBySectionId(Long sectionId);
    QuestionGroupAdminResponse createGroup(Long sectionId, QuestionGroupAdminRequest request);
    QuestionGroupAdminResponse updateGroup(Long groupId, QuestionGroupAdminRequest request);
    void deleteGroup(Long groupId);
}

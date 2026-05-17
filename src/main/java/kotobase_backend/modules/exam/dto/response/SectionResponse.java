package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.SectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {
    private Long id;
    private SectionType sectionType;
    private int minutes;
    private List<QuestionGroupResponse> questionGroups;
}

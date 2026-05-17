package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionGroupResponse {
    private Long id;
    private GroupType groupType;
    private String content;
    private Integer displayOrder;
    private String audioUrl;
    private String imageUrl;
    private List<QuestionResponse> questions;
}

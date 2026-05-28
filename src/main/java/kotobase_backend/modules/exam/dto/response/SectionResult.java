package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.SectionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SectionResult {
    private Long setionId;
    private String setionName;
    private Boolean isPass;
    private SectionType sectionType;
    private BigDecimal score;
    private BigDecimal maxScore;
}

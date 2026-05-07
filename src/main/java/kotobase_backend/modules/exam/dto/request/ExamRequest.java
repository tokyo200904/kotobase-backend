package kotobase_backend.modules.exam.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExamRequest {
    @NotNull(message = "levelId khong duoc de trong")
    private Integer levelId;

    @Min(value = 1, message = "page phai >= 1")
    private int page = 1;

    @Min(value = 1, message = "limit phai >= 1")
    private int limit = 10;
}

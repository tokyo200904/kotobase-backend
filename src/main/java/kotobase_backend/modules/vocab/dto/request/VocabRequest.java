package kotobase_backend.modules.vocab.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VocabRequest {

    @NotNull(message = "topicId khong duoc de trong")
    private Integer topicId;

    @Min(value = 1, message = "page phai >= 1")
    private int page = 1;

    @Min(value = 1, message = "limit phai >= 1")
    private int limit = 10;
}

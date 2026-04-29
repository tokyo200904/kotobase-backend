package kotobase_backend.modules.vocab.mapper;

import kotobase_backend.modules.example.dto.response.ExampleResponse;
import kotobase_backend.modules.example.mapper.ExampleMapper;
import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VocabMapper {
    private final ExampleMapper exampleMapper;

    public VocabResponse mapToVocab(Vocab v) {
        List<ExampleResponse> examples =
                v.getExamples() == null ? List.of() : v.getExamples().stream()
                        .filter(r -> r.getTargetType().name().equals("vocab"))
                        .map(exampleMapper::toExampleResponse)
                        .toList();

        VocabResponse response = new VocabResponse();
        response.setId(v.getId());
        response.setWord(v.getWord());
        response.setReading(v.getReading());
        response.setMeaning(v.getMeaning());
        response.setRomaji(v.getRomaji());
        response.setExamples(examples);

        return response;
    }
}

package kotobase_backend.modules.vocab.mapper;

import kotobase_backend.modules.vocab.dto.response.VocabResponse;
import kotobase_backend.modules.vocab.entity.Vocab;
import org.springframework.stereotype.Component;

@Component
public class VocabMapper {
    public VocabResponse mapToVocab(Vocab entity) {
        VocabResponse response = new VocabResponse();
        response.setId(entity.getId());
//        response.setExample(entity.getExample());
//        response.setKanji(entity.getKanji());
        response.setReading(entity.getReading());
        response.setMeaning(entity.getMeaning());
        return response;
    }
}

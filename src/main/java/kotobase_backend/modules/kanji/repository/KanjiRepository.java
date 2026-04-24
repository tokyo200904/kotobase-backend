package kotobase_backend.modules.kanji.repository;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjisResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Integer> {

    @Query("select k.characters from Kanji k where k.level_id like %:level%")
    public List<KanjisResponse> findByLevel(@Param("level") Level level);


}

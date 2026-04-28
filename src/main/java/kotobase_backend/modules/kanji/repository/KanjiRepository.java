package kotobase_backend.modules.kanji.repository;
import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KanjiRepository extends JpaRepository<Kanji, Integer> {

    @Query("SELECT k FROM Kanji k WHERE k.level.level = :level")
    public List<Kanji> findByLevel(@Param("level") Level level);

    @Query("SELECT k FROM Kanji k JOIN FETCH k.readings where k.id = :id")
    Optional<Kanji> getKanjiByID(@Param("id") Integer id);

    @Query("Select k FROM Kanji k LEFT JOIN FETCH k.readings kr " +
            "where k.characters like :key% " +
            "or k.meaning like :key% " +
            "or k.han like :key% ")
    public List<Kanji> findKanji(@Param("key")  String key);
}

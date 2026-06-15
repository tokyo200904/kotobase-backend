package kotobase_backend.modules.kanji.repository;
import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.kanji.dto.Response.KanjiDetelResponse;
import kotobase_backend.modules.kanji.entity.Kanji;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT DISTINCT k FROM Kanji k LEFT JOIN FETCH k.readings where k.id = :id")
    Optional<Kanji> getKanjiByID(@Param("id") Integer id);

    @Query("Select k FROM Kanji k LEFT JOIN FETCH k.readings kr " +
            "where k.characters like :key% " +
            "or k.meaning like :key% " +
            "or k.han like :key% ")
    public List<Kanji> findKanji(@Param("key")  String key);

    @Query(value = "SELECT * FROM kanjis k " +
            "WHERE k.level_id = :levelId AND k.id != :correctId " +
            "ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Kanji> findRandomDistractors(@Param("levelId") Integer levelId, @Param("correctId") Integer correctId);

    List<Kanji> findByLevel_Id(Integer levelId);

    @Query("SELECT k FROM Kanji k WHERE " +
            "(:search IS NULL OR k.characters LIKE %:search% OR k.meaning LIKE %:search%) " +
            "AND (:levelId IS NULL OR k.level.id = :levelId) " +
            "ORDER BY k.id DESC")
    Page<Kanji> adminSearchKanjis(@Param("search") String search,
                                  @Param("levelId") Integer levelId,
                                  Pageable pageable);

    List<Kanji> findByLevel_IdOrderByIdAsc(Integer levelId);
}

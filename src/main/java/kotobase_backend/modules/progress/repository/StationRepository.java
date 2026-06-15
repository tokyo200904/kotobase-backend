package kotobase_backend.modules.progress.repository;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.entity.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    List<Station> findByLevelIdAndItemTypeOrderByStationOrderAsc(Integer levelId, ItemType itemType);

    @Query("SELECT s FROM Station s WHERE " +
            "(:search IS NULL OR s.title LIKE %:search%) " +
            "AND (:levelId IS NULL OR s.level.id = :levelId) " +
            "AND (:itemType IS NULL OR s.itemType = :itemType) " +
            "ORDER BY s.level.id ASC, s.itemType ASC, s.stationOrder ASC")
    Page<Station> adminSearchStations(@Param("search") String search,
                                      @Param("levelId") Integer levelId,
                                      @Param("itemType") ItemType itemType,
                                      Pageable pageable);
}
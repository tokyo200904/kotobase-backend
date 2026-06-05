package kotobase_backend.modules.progress.repository;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    List<Station> findByLevelIdAndItemTypeOrderByStationOrderAsc(Integer levelId, ItemType itemType);
}

package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, Integer> {
}

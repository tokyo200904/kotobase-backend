package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

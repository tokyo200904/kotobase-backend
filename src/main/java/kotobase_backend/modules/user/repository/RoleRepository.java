package kotobase_backend.modules.user.repository;

import kotobase_backend.comom.enums.RoleName;
import kotobase_backend.modules.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Optional<Role> findByRole(RoleName roleName);
}

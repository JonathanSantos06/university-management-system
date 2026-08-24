package mx.edu.sgu.auth.repository;

import mx.edu.sgu.auth.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
}

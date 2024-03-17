package com.dersler.mycompany.repository;

import com.dersler.mycompany.entity.RoleEntity;
import com.dersler.mycompany.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(ERole erole);

}

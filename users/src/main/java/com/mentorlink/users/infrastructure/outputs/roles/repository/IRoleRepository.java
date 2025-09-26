package com.mentorlink.users.infrastructure.outputs.roles.repository;

import com.mentorlink.users.domain.enums.RoleName;
import com.mentorlink.users.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(RoleName name);
}

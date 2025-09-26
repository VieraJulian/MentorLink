package com.mentorlink.users.infrastructure.outputs.roles.persistence;

import com.mentorlink.users.domain.enums.RoleName;
import com.mentorlink.users.domain.model.Role;
import com.mentorlink.users.domain.port.outbound.IRoleSpiPort;
import com.mentorlink.users.infrastructure.outputs.roles.repository.IRoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleRepositoryImpl implements IRoleSpiPort {

    private final IRoleRepository iRoleRepository;

    public RoleRepositoryImpl(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    @Override
    public Optional<Role> getRoleByName(RoleName roleName) {
        return iRoleRepository.findRoleByName(roleName);
    }
}

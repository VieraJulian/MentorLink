package com.mentorlink.users.domain.port.outbound;

import com.mentorlink.users.domain.enums.RoleName;
import com.mentorlink.users.domain.model.Role;

import java.util.Optional;

public interface IRoleSpiPort {

    Optional<Role> getRoleByName(RoleName roleName);
}

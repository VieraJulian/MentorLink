package com.mentorlink.users.application.service.mapper;

import com.mentorlink.users.domain.model.Role;
import com.mentorlink.users.domain.model.User;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "externalId", source = "userIdentity.id"),
            @Mapping(target = "role", source = "role")
    })
    User userIdentityToUser(UserIdentity userIdentity, String country, String province, String timezone, Role role);

    @Mapping(target = "role", source = "role.name")
    UserResponse userToUserResponse(User user);
}


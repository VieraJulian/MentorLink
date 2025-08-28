package com.mentorlink.users.application.service.auth.mapper;

import com.mentorlink.users.infrastructure.inputs.auth.dto.CreateUserRequest;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;
import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IAuthRequestMapper {

    @Mapping(target = "role", expression = "java(role)")
    CreateIdentity createUserRequestToCreateIdentity(CreateUserRequest createUserRequest, String role);

    UserResponse userIdentityToUserResponse(UserIdentity userIdentity);
}

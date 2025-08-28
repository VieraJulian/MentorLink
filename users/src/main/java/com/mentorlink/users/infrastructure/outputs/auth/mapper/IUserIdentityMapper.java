package com.mentorlink.users.infrastructure.outputs.auth.mapper;

import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserIdentityMapper {

    @Mapping(target = "firstname", source = "userRepresentation.firstName")
    @Mapping(target = "lastname", source = "userRepresentation.lastName")
    @Mapping(target = "role", expression = "java(role)")
    UserIdentity userRepresentationToUserIdentity(UserRepresentation userRepresentation, String role);
}

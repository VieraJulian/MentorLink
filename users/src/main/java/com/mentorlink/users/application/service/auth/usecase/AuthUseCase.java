package com.mentorlink.users.application.service.auth.usecase;

import com.mentorlink.users.application.service.auth.mapper.IAuthMapper;
import com.mentorlink.users.application.service.exception.AuthProviderUserCreationException;
import com.mentorlink.users.application.service.exception.RoleNotFoundException;
import com.mentorlink.users.application.service.exception.UserCreationException;
import com.mentorlink.users.application.service.users.mapper.IUserMapper;
import com.mentorlink.users.domain.enums.RoleName;
import com.mentorlink.users.domain.port.outbound.ITimezone;
import com.mentorlink.users.domain.model.Role;
import com.mentorlink.users.domain.model.User;
import com.mentorlink.users.domain.port.inbound.IAuthApiPort;
import com.mentorlink.users.domain.port.outbound.IAuthProvider;
import com.mentorlink.users.domain.port.outbound.IRoleSpiPort;
import com.mentorlink.users.domain.port.outbound.IUserSpiPort;
import com.mentorlink.users.infrastructure.inputs.auth.dto.CreateUserRequest;
import com.mentorlink.users.infrastructure.inputs.auth.dto.LoginUserRequest;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;
import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import org.springframework.stereotype.Service;

@Service
public class AuthUseCase implements IAuthApiPort {

    private final IAuthProvider iAuthProvider;

    private final IUserSpiPort iUserSpiPort;

    private final IRoleSpiPort iRoleSpiPort;

    private final ITimezone iTimezone;

    private final IAuthMapper iAuthMapper;

    private final IUserMapper iUserMapper;

    public AuthUseCase(IAuthProvider iAuthProvider, IAuthMapper iAuthMapper, IUserSpiPort iUserSpiPort, IRoleSpiPort iRoleSpiPort, ITimezone iTimezone, IUserMapper iUserMapper) {
        this.iUserMapper = iUserMapper;
        this.iTimezone = iTimezone;
        this.iRoleSpiPort = iRoleSpiPort;
        this.iUserSpiPort = iUserSpiPort;
        this.iAuthProvider = iAuthProvider;
        this.iAuthMapper = iAuthMapper;
    }

    @Override public UserResponse register(CreateUserRequest createUserRequest) {
        String email = createUserRequest.email();

        String role = email.contains("@mentorlink.com") ? RoleName.ADMIN.name() : RoleName.CLIENT.name();

        CreateIdentity createIdentity = iAuthMapper.createUserRequestToCreateIdentity(createUserRequest, role);

        UserIdentity userIdentity = iAuthProvider.createUser(createIdentity);

        if (userIdentity == null) {
            throw new AuthProviderUserCreationException("Error creating user with auth provider");
        }

        try {
            Role userRole = iRoleSpiPort.getRoleByName(RoleName.valueOf(userIdentity.role().toUpperCase()))
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: " + userIdentity.role()));

            String timezone = iTimezone.getTimezoneByCountry(createUserRequest.country());

            String country = createUserRequest.country();

            String province = createUserRequest.province();

            User userInfo = iUserMapper.userIdentityToUser(userIdentity, country, province, timezone, userRole);

            User userCreated = iUserSpiPort.saveUser(userInfo);

            return iUserMapper.userToUserResponse(userCreated);

        } catch (Exception e) {
            iAuthProvider.deleteUser(userIdentity.id());
            throw new UserCreationException("Error creating user");
        }
    }

    @Override
    public String login(LoginUserRequest loginUserRequest) {
        return iAuthProvider.loginUser(loginUserRequest.username(), loginUserRequest.password());
    }
}

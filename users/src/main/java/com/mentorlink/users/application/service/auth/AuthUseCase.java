package com.mentorlink.users.application.service.auth;

import com.mentorlink.users.application.service.auth.mapper.IAuthRequestMapper;
import com.mentorlink.users.domain.enums.RoleName;
import com.mentorlink.users.domain.port.inbound.IAuthApiPort;
import com.mentorlink.users.domain.port.outbound.IAuthProvider;
import com.mentorlink.users.infrastructure.inputs.auth.dto.CreateUserRequest;
import com.mentorlink.users.infrastructure.inputs.auth.dto.LoginUserRequest;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;
import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUseCase implements IAuthApiPort {

    private final IAuthProvider iAuthProvider;

    private final IAuthRequestMapper iAuthRequestMapper;

    public AuthUseCase(IAuthProvider iAuthProvider, IAuthRequestMapper iAuthRequestMapper) {
        this.iAuthProvider = iAuthProvider;
        this.iAuthRequestMapper = iAuthRequestMapper;
    }

    @Override
    public UserResponse register(CreateUserRequest createUserRequest) {
        String email = createUserRequest.email();

        String role = email.contains("@mentorlink.com") ? RoleName.ADMIN.name().toLowerCase() : RoleName.CLIENT.name().toLowerCase();

        CreateIdentity createIdentity = iAuthRequestMapper.createUserRequestToCreateIdentity(createUserRequest, role);

        UserIdentity userIdentity = iAuthProvider.createUser(createIdentity);

        return iAuthRequestMapper.userIdentityToUserResponse(userIdentity);
    }

    @Override
    public String login(LoginUserRequest loginUserRequest) {
        return iAuthProvider.loginUser(loginUserRequest.username(), loginUserRequest.password());
    }
}

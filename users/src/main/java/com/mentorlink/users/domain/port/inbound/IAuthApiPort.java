package com.mentorlink.users.domain.port.inbound;

import com.mentorlink.users.infrastructure.inputs.auth.dto.CreateUserRequest;
import com.mentorlink.users.infrastructure.inputs.auth.dto.LoginUserRequest;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;

public interface IAuthApiPort {

    UserResponse register(CreateUserRequest createUserRequest);

    String login(LoginUserRequest loginUserRequest);
}

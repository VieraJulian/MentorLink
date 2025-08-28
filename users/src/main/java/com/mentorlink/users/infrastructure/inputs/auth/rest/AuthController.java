package com.mentorlink.users.infrastructure.inputs.auth.rest;

import com.mentorlink.users.domain.port.inbound.IAuthApiPort;
import com.mentorlink.users.infrastructure.inputs.auth.dto.CreateUserRequest;
import com.mentorlink.users.infrastructure.inputs.common.response.ApiResponse;
import com.mentorlink.users.infrastructure.inputs.common.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthApiPort iAuthApiPort;

    public AuthController(IAuthApiPort iAuthApiPort) {
        this.iAuthApiPort = iAuthApiPort;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody CreateUserRequest createUserRequest){
            UserResponse userResponse = iAuthApiPort.register(createUserRequest);

            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .status("success")
                    .message("User registered successfully")
                    .data(userResponse)
                    .metadata(null)
                    .build();

            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

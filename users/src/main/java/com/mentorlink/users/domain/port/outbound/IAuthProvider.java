package com.mentorlink.users.domain.port.outbound;

import com.mentorlink.users.infrastructure.outputs.auth.dto.CreateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UpdateIdentity;
import com.mentorlink.users.infrastructure.outputs.auth.dto.UserIdentity;

import java.util.Optional;

public interface IAuthProvider {

    UserIdentity createUser(CreateIdentity createIdentity);
    UserIdentity updateUser(String id, UpdateIdentity updateIdentity);
    void deleteUser(String id);
    Optional<UserIdentity> findUserById(String id);
    String loginUser(String username, String password);

}

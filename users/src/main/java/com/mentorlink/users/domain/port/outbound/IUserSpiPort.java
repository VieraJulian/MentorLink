package com.mentorlink.users.domain.port.outbound;

import com.mentorlink.users.domain.model.User;

import java.util.Optional;

public interface IUserSpiPort {

    User saveUser(User user);
    Optional<User> getUserById(Long id);
    void deleteUserById(Long id);
}

package com.mentorlink.users.infrastructure.outputs.users.persistence;

import com.mentorlink.users.domain.model.User;
import com.mentorlink.users.domain.port.outbound.IUserSpiPort;
import com.mentorlink.users.infrastructure.outputs.users.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryImpl implements IUserSpiPort {

    private final IUserRepository userRepository;

    public UserRepositoryImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}

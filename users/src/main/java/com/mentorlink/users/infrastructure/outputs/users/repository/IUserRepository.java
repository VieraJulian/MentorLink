package com.mentorlink.users.infrastructure.outputs.users.repository;

import com.mentorlink.users.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
}

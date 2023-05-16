package com.dir.music.authentication_service.repository.user_repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    boolean existsByUsername(String username);

    UserModel findByUsername(String username);

    UserModel findById(long id);

    void deleteById(long id);
}

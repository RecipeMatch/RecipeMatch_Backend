package org.example.recipe_match_backend.user.repository;

import org.example.recipe_match_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // googleUid로 유저 찾기
    Optional<User> findByGoogleUid(String googleUid);

    // email로 유저 찾기
    Optional<User> findBuEmail(String email);

}

package org.example.recipe_match_backend.user.repository;

import org.example.recipe_match_backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

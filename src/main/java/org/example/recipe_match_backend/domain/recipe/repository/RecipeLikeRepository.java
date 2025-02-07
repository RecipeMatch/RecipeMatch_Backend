package org.example.recipe_match_backend.domain.recipe.repository;

import org.example.recipe_match_backend.domain.recipe.domain.Recipe;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeLike;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RecipeLikeRepository extends JpaRepository<RecipeLike,Long>{
    Optional<RecipeLike> findByUserAndRecipe(User user, Recipe recipe);

    @Modifying
    void deleteByUserAndRecipe(User user,Recipe recipe);
}

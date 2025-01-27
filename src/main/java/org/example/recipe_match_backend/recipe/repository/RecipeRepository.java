package org.example.recipe_match_backend.recipe.repository;

import org.example.recipe_match_backend.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe,Long> {
}

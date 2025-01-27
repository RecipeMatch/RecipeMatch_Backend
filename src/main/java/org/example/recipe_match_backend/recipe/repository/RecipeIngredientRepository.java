package org.example.recipe_match_backend.recipe.repository;

import org.example.recipe_match_backend.recipe.domain.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient,Long> {
}

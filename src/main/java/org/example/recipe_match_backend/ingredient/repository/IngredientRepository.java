package org.example.recipe_match_backend.ingredient.repository;

import org.example.recipe_match_backend.ingredient.domain.Ingredient;
import org.example.recipe_match_backend.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
    Optional<Ingredient> findByIngredientName(String ingredientName);
}
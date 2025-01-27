package org.example.recipe_match_backend.ingredient.repository;

import org.example.recipe_match_backend.ingredient.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {
    Ingredient findByIngredientName(String ingredientName);
}

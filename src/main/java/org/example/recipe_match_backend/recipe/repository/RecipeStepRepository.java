package org.example.recipe_match_backend.recipe.repository;

import org.example.recipe_match_backend.recipe.domain.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep,Long> {
}

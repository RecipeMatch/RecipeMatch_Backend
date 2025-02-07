package org.example.recipe_match_backend.domain.recipe.repository;

import org.example.recipe_match_backend.domain.recipe.domain.RecipeRating;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 레시피 별점 관련 리포지토리
 */
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {

}

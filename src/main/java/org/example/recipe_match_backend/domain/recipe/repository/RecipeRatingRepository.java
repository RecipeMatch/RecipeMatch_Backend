package org.example.recipe_match_backend.domain.recipe.repository;

import org.example.recipe_match_backend.domain.recipe.domain.Recipe;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeRating;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 레시피 별점 관련 리포지토리
 */
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {

    // 사용자가 입력한 각 레시피의 평균 별점을 계산 (평균이 없으면 null이 반환됨.)
    @Query("SELECT AVG(r.ratingValue) FROM RecipeRating r WHERE r.recipe.id = :recipeId")
    Double findAverageRatingByRecipeId(@Param("recipeId") Long recipeId);

    // 특정 레시피에 대해 사용자가 등록한 별점이 있는지 확인
    Optional<RecipeRating> findByRecipeAndUser(Recipe recipe, User user);

}

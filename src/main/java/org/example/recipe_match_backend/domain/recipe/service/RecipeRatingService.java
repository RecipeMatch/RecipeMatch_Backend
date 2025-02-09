package org.example.recipe_match_backend.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.domain.Recipe;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeRating;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating.RecipeIdRequest;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating.RecipeRatingRequest;
import org.example.recipe_match_backend.domain.recipe.repository.RecipeRatingRepository;
import org.example.recipe_match_backend.domain.recipe.repository.RecipeRepository;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.example.recipe_match_backend.domain.user.repository.UserRepository;
import org.example.recipe_match_backend.global.exception.recipe.RecipeNotFoundException;
import org.example.recipe_match_backend.global.exception.user.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeRatingService {
    private final RecipeRatingRepository recipeRatingRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;


    /**
     * 레시피에 별점을 등록하거나 이미 존재하면 수정한다,
     */
    @Transactional
    public void rateRecipe(Long recipeId, RecipeRatingRequest request){
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(RecipeNotFoundException::new);
        User user = userRepository.findByUid(request.getUserUid())
                .orElseThrow(UserNotFoundException::new);

        RecipeRating recipeRating = recipeRatingRepository.findByRecipeAndUser(recipe, user)
                .orElse(null);

        if (recipeRating != null) {
            recipeRating.updateRating(request.getRatingValue());
        } else {
            // RecipeRating 엔티티에 Builder를 사용하기 위해 @Builder 어노테이션 추가되어 있음
            recipeRating = RecipeRating.builder()
                    .ratingValue(request.getRatingValue())
                    .user(user)
                    .recipe(recipe)
                    .build();
        }
        recipeRatingRepository.save(recipeRating);

    }

    /**
     * 특정 레시피에 대한 평균 별점을 반환한다.
     */
    public Double getAverageRating(Long recipeId){
        Double avg = recipeRatingRepository.findAverageRatingByRecipeId(recipeId);
        return (avg != null) ? avg : 0.0;

    }
}

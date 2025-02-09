package org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class RecipeRatingRequest {
    private String userUid;
    private int ratingValue;

}

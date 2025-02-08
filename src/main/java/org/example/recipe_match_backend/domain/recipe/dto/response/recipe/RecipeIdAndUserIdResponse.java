package org.example.recipe_match_backend.domain.recipe.dto.response.recipe;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RecipeIdAndUserIdResponse {
    private Long recipeId;
    private Long userId;
}

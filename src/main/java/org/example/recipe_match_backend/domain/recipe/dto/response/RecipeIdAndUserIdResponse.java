package org.example.recipe_match_backend.domain.recipe.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecipeIdAndUserIdResponse {
    private Long UserId;
    private Long RecipeId;

}

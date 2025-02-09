package org.example.recipe_match_backend.domain.recipe.dto.response.recipe;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecipeIdAndUserUidResponse {
    private String UserUid;
    private Long RecipeId;

}

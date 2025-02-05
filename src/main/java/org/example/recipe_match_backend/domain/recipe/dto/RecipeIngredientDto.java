package org.example.recipe_match_backend.domain.recipe.dto;

import lombok.*;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeIngredient;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RecipeIngredientDto {

    private String quantity;
    private String ingredientName;

    public RecipeIngredientDto(RecipeIngredient recipeIngredient){
        this.quantity = recipeIngredient.getQuantity();
        this.ingredientName = recipeIngredient.getIngredient().getIngredientName();
    }

}

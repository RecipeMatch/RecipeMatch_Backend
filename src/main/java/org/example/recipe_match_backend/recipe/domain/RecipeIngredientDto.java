package org.example.recipe_match_backend.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeIngredientDto {

    private String quantity;
    private String IngredientName;

    public RecipeIngredientDto(RecipeIngredient recipeIngredient){
        this.quantity = recipeIngredient.getQuantity();
        this.IngredientName = recipeIngredient.getIngredient().getIngredientName();
    }

}

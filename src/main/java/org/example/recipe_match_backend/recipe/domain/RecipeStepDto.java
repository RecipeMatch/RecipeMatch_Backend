package org.example.recipe_match_backend.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecipeStepDto {

    private int stepOrder;

    private String content; // 단계별 설명

    public RecipeStepDto(RecipeStep recipeStep){
        this.stepOrder = recipeStep.getStepOrder();
        this.content = recipeStep.getContent();
    }
}

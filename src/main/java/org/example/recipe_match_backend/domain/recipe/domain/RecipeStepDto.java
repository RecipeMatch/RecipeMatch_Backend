package org.example.recipe_match_backend.domain.recipe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class RecipeStepDto {

    private int stepOrder;

    private String content; // 단계별 설명

    public RecipeStepDto(RecipeStep recipeStep){
        this.stepOrder = recipeStep.getStepOrder();
        this.content = recipeStep.getContent();
    }
}

package org.example.recipe_match_backend.recipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.recipe_match_backend.type.CategoryType;
import org.example.recipe_match_backend.type.DifficultyType;

import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecipeDto {

    private String recipeName;//Recipe

    private String description;//Recipe

    private int cookingTime;//Recipe

    private DifficultyType difficulty;//Recipe

    private CategoryType category;//Recipe

    private List<RecipeIngredientDto> recipeIngredientDtos;//RecipeIngredient

    private List<RecipeStepDto> recipeStepDtos;//RecipeStep

    private List<String> toolName;//RecipeTool

    public RecipeDto(Recipe recipe){
        this.recipeName = recipe.getRecipeName();
        this.description = recipe.getDescription();
        this.cookingTime = recipe.getCookingTime();
        this.difficulty = recipe.getDifficulty();
        this.category = recipe.getCategory();
        this.recipeIngredientDtos = recipe.getRecipeIngredients().stream().map(r->new RecipeIngredientDto(r)).collect(toList());
        this.recipeStepDtos = recipe.getRecipeSteps().stream().map(s -> new RecipeStepDto(s)).collect(toList());
        for(RecipeTool recipeTool:recipe.getRecipeTools()){
            this.toolName.add(recipeTool.getTool().getToolName());
        }
    }
}

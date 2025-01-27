package org.example.recipe_match_backend.recipe.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.ingredient.domain.Ingredient;
import org.example.recipe_match_backend.ingredient.repository.IngredientRepository;
import org.example.recipe_match_backend.recipe.domain.*;
import org.example.recipe_match_backend.recipe.repository.RecipeRepository;
import org.example.recipe_match_backend.tool.domain.Tool;
import org.example.recipe_match_backend.tool.repository.ToolRepository;
import org.example.recipe_match_backend.user.domain.User;
import org.example.recipe_match_backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public void save(RecipeDto recipeDto,Long userId){

        //재료와 도구 추가하기(중복되지 않도록)

        User user = userRepository.findById(userId).get();//사용자 검색

        Recipe recipe = Recipe.builder().recipeName(recipeDto.getRecipeName()).description(recipeDto.getDescription())
                .cookingTime(recipeDto.getCookingTime()).difficulty(recipeDto.getDifficulty()).category(recipeDto.getCategory())
                .user(user).build();//레시피 DTO를 레시피 엔티티로 매핑

        List<RecipeTool> recipeTools = new ArrayList<>();
        for(String toolName: recipeDto.getToolName()){
            Tool tool = toolRepository.findByToolName(toolName);
            RecipeTool recipeTool = RecipeTool.builder().tool(tool).recipe(recipe).build();
            recipeTools.add(recipeTool);
            tool.addRecipeTool(recipeTool);
        }//입력받은 요리도구를 데이터베이스에서 찾아 연관관계 주입 후 List로 바꾸어 레시피 엔티티에 주입 준비

        List<RecipeStep> recipeSteps = recipeDto.getRecipeStepDtos().stream().map(s -> RecipeStep.builder()
                .stepOrder(s.getStepOrder()).content(s.getContent()).recipe(recipe)
                .build()).collect(toList());
        //RecipeStepDto를 RecipeStep엔티티에 매핑하여 List 형변환

        List<RecipeIngredient> recipeIngredients = recipeDto.getRecipeIngredientDtos().stream().map(i -> RecipeIngredient
                .builder().quantity(i.getQuantity()).recipe(recipe)
                .ingredient(ingredientRepository.findByIngredientName(i.getIngredientName()))
                .build()).collect(toList());
        //RecipeIngredientDto를 ecipeIngredient엔티티에 매핑하여 List 형변환

        for(RecipeIngredient recipeIngredient: recipeIngredients){
            Ingredient ingredient = recipeIngredient.getIngredient();
            ingredient.addRecipeIngredient(recipeIngredient);
        }

        Recipe finalRecipe = recipe.toBuilder().recipeSteps(recipeSteps).recipeIngredients(recipeIngredients).recipeTools(recipeTools).build();
        //마지막으로 레시피 엔티티에 각 엔티티 리스트 주입

        recipeRepository.save(finalRecipe);
    }

    public void update(Long id){

    }

    @Transactional
    public void delete(Long id){

    }

    public void find(Long id){

    }

}

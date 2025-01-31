package org.example.recipe_match_backend.domain.recipe.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.recipe_match_backend.domain.ingredient.domain.Ingredient;
import org.example.recipe_match_backend.domain.ingredient.repository.IngredientRepository;
import org.example.recipe_match_backend.domain.recipe.domain.*;
import org.example.recipe_match_backend.domain.recipe.dto.RecipeIngredientDto;
import org.example.recipe_match_backend.domain.recipe.dto.RecipeStepDto;
import org.example.recipe_match_backend.domain.recipe.dto.request.RecipeRequest;
import org.example.recipe_match_backend.domain.recipe.dto.response.RecipeResponse;
import org.example.recipe_match_backend.domain.recipe.repository.RecipeRepository;
import org.example.recipe_match_backend.domain.tool.domain.Tool;
import org.example.recipe_match_backend.domain.tool.repository.ToolRepository;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.example.recipe_match_backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public Long save(RecipeRequest recipeRequest, Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Recipe 엔티티 생성
        Recipe recipe = Recipe.builder()
                .recipeName(recipeRequest.getRecipeName())
                .description(recipeRequest.getDescription())
                .cookingTime(recipeRequest.getCookingTime())
                .difficulty(recipeRequest.getDifficulty())
                .category(recipeRequest.getCategory())
                .recipeIngredients(new ArrayList<>())
                .recipeSteps(new ArrayList<>())
                .recipeTools(new ArrayList<>())
                .user(user)
                .build();

        // 사용자와 레시피 관계 설정
        user.addRecipe(recipe);

        // Ingredients 처리
        for (RecipeIngredientDto dto : recipeRequest.getRecipeIngredientDtos()) {
            // 기존 Ingredient 조회 또는 새로 생성
            Ingredient ingredient = ingredientRepository.findByIngredientName(dto.getIngredientName())
                    .orElseGet(() -> {
                        Ingredient newIngredient = Ingredient.builder()
                                .ingredientName(dto.getIngredientName())
                                .recipeIngredients(new ArrayList<>())
                                .userIngredients(new ArrayList<>())
                                .build();
                        return ingredientRepository.save(newIngredient);
                    });

            // RecipeIngredient 생성
            RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                    .quantity(dto.getQuantity())
                    .ingredient(ingredient)
                    .build();

            // 양방향 관계 설정
            recipe.addRecipeIngredient(recipeIngredient);
            ingredient.addRecipeIngredient(recipeIngredient);
        }


        // Tools 처리
        for (String toolName : recipeRequest.getToolName()) {
            Tool tool = toolRepository.findByToolName(toolName)
                    .orElseGet(() -> {
                        Tool newTool = Tool.builder()
                                .toolName(toolName)
                                .recipeTools(new ArrayList<>())
                                .userTools(new ArrayList<>())
                                .build();
                        return toolRepository.save(newTool);
                    });

            // RecipeTool 생성
            RecipeTool recipeTool = RecipeTool.builder()
                    .tool(tool)
                    .build();

            // 양방향 관계 설정
            recipe.addRecipeTool(recipeTool);
            tool.addRecipeTool(recipeTool);
        }

        // RecipeSteps 처리
        for (RecipeStepDto stepDto : recipeRequest.getRecipeStepDtos()) {
            RecipeStep step = RecipeStep.builder()
                    .stepOrder(stepDto.getStepOrder())
                    .content(stepDto.getContent())
                    .build();
            recipe.addRecipeStep(step);
        }

        // Recipe 저장 (CascadeType.PERSIST에 의해 연관된 엔티티들도 함께 저장됨)
        Recipe savedRecipe = recipeRepository.save(recipe);

        return savedRecipe.getId();
    }

    @Transactional
    public Long update(Long recipeId, RecipeRequest recipeRequest){

        Recipe recipe = recipeRepository.findById(recipeId).get();

        //null체크
        if(recipeRequest.getRecipeName() != null){
            recipe.setRecipeName(recipeRequest.getRecipeName());
        }
        if(recipeRequest.getCategory() != null){
            recipe.setCategory(recipeRequest.getCategory());
        }
        if(recipeRequest.getDifficulty() != null){
            recipe.setDifficulty(recipeRequest.getDifficulty());
        }
        if(recipeRequest.getDescription() != null){
            recipe.setDescription(recipeRequest.getDescription());
        }
        if(recipeRequest.getCookingTime() != null){
            recipe.setCookingTime(recipeRequest.getCookingTime());
        }
        if(recipeRequest.getToolName() != null){

            //수정된 toolName db에 저장(중복 제외)
            for (String toolName : recipeRequest.getToolName()) {
                Tool tool = toolRepository.findByToolName(toolName)
                        .orElseGet(() -> {
                            Tool newTool = Tool.builder()
                                    .toolName(toolName)
                                    .recipeTools(new ArrayList<>())
                                    .userTools(new ArrayList<>())
                                    .build();
                            return toolRepository.save(newTool);
                        });

                // RecipeTool 생성
                RecipeTool recipeTool = RecipeTool.builder()
                        .tool(tool)
                        .build();

                // 양방향 관계 설정
                recipe.addRecipeTool(recipeTool);//문제는 이러면 추가만 가능하고 기존의 도구는 안사라짐
                tool.addRecipeTool(recipeTool);//그러면 프론트에서 해당 도구의 id를 dto로 받고 지워버리기
            }

        }
        if(recipeRequest.getRecipeIngredientDtos() != null){

            //수정된 재료 db에 저장(중복 제외)
            for (RecipeIngredientDto dto : recipeRequest.getRecipeIngredientDtos()) {
                // 기존 Ingredient 조회 또는 새로 생성
                Ingredient ingredient = ingredientRepository.findByIngredientName(dto.getIngredientName())
                        .orElseGet(() -> {
                            Ingredient newIngredient = Ingredient.builder()
                                    .ingredientName(dto.getIngredientName())
                                    .recipeIngredients(new ArrayList<>())
                                    .userIngredients(new ArrayList<>())
                                    .build();
                            return ingredientRepository.save(newIngredient);
                        });

                // RecipeIngredient 생성
                RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                        .quantity(dto.getQuantity())
                        .ingredient(ingredient)
                        .build();

                // 양방향 관계 설정
                recipe.addRecipeIngredient(recipeIngredient);//문제는 이러면 추가만 가능하고 기존의 재료는 안사라짐
                ingredient.addRecipeIngredient(recipeIngredient);//그러면 프론트에서 해당 재료의 id를 dto로 받고 지워버리기
            }
        }
        for (RecipeStepDto stepDto : recipeRequest.getRecipeStepDtos()) {
            RecipeStep step = RecipeStep.builder()
                    .stepOrder(stepDto.getStepOrder())
                    .content(stepDto.getContent())
                    .build();
            recipe.addRecipeStep(step);//문제는 이러면 추가만 가능하고 기존의 단계는 안사라짐
        }                               //그러면 프론트에서 해당 단계의 id를 dto로 받고 지워버리기

        recipe.toBuilder().build();

        return recipe.getId();
    }

    @Transactional
    public void delete(Long recipeId){
        recipeRepository.deleteById(recipeId);
    }

    public RecipeResponse find(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId).get();
        return new RecipeResponse(recipe);
    }

    public List<RecipeResponse> findAll(){
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(r -> new RecipeResponse(r)).collect(toList());
    }

}


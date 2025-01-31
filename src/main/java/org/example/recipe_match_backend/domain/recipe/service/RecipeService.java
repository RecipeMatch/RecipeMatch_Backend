package org.example.recipe_match_backend.domain.recipe.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.recipe_match_backend.domain.ingredient.domain.Ingredient;
import org.example.recipe_match_backend.domain.ingredient.repository.IngredientRepository;
import org.example.recipe_match_backend.domain.recipe.domain.*;
import org.example.recipe_match_backend.recipe.domain.*;
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
    public Long save(RecipeDto recipeDto, Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Recipe 엔티티 생성
        Recipe recipe = Recipe.builder()
                .recipeName(recipeDto.getRecipeName())
                .description(recipeDto.getDescription())
                .cookingTime(recipeDto.getCookingTime())
                .difficulty(recipeDto.getDifficulty())
                .category(recipeDto.getCategory())
                .recipeIngredients(new ArrayList<>())
                .recipeSteps(new ArrayList<>())
                .recipeTools(new ArrayList<>())
                .user(user)
                .build();

        // 사용자와 레시피 관계 설정
        user.addRecipe(recipe);

        // Ingredients 처리
        for (RecipeIngredientDto dto : recipeDto.getRecipeIngredientDtos()) {
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
        for (String toolName : recipeDto.getToolName()) {
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
        for (RecipeStepDto stepDto : recipeDto.getRecipeStepDtos()) {
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
    public Long update(Long recipeId,RecipeDto recipeDto){

        Recipe recipe = recipeRepository.findById(recipeId).get();

        //null체크
        if(recipeDto.getRecipeName() != null){
            recipe.setRecipeName(recipeDto.getRecipeName());
        }
        if(recipeDto.getCategory() != null){
            recipe.setCategory(recipeDto.getCategory());
        }
        if(recipeDto.getDifficulty() != null){
            recipe.setDifficulty(recipeDto.getDifficulty());
        }
        if(recipeDto.getDescription() != null){
            recipe.setDescription(recipeDto.getDescription());
        }
        if(recipeDto.getCookingTime() != null){
            recipe.setCookingTime(recipeDto.getCookingTime());
        }
        if(recipeDto.getToolName() != null){

            //수정된 toolName db에 저장(중복 제외)
            for (String toolName : recipeDto.getToolName()) {
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
        if(recipeDto.getRecipeIngredientDtos() != null){

            //수정된 재료 db에 저장(중복 제외)
            for (RecipeIngredientDto dto : recipeDto.getRecipeIngredientDtos()) {
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
        for (RecipeStepDto stepDto : recipeDto.getRecipeStepDtos()) {
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

    public RecipeDto find(Long recipeId){
        Recipe recipe = recipeRepository.findById(recipeId).get();
        return new RecipeDto(recipe);
    }

    public List<RecipeDto> findAll(){
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream().map(r -> new RecipeDto(r)).collect(toList());
    }

}


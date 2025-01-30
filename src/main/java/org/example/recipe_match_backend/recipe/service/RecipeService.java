package org.example.recipe_match_backend.recipe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.recipe_match_backend.ingredient.domain.Ingredient;
import org.example.recipe_match_backend.ingredient.repository.IngredientRepository;
import org.example.recipe_match_backend.recipe.domain.*;
import org.example.recipe_match_backend.recipe.repository.RecipeRepository;
import org.example.recipe_match_backend.tool.domain.Tool;
import org.example.recipe_match_backend.tool.repository.ToolRepository;
import org.example.recipe_match_backend.user.domain.User;
import org.example.recipe_match_backend.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void update(Long recipeId,RecipeDto recipeDto){
        Recipe recipe = recipeRepository.findById(recipeId).get();

        Recipe.RecipeBuilder updateRecipe = recipe.toBuilder();

        //null체크
        if(recipeDto.getRecipeName() != null){
            updateRecipe.recipeName(recipeDto.getRecipeName());
        }
        if(recipeDto.getCategory() != null){
            updateRecipe.category(recipeDto.getCategory());
        }
        if(recipeDto.getDifficulty() != null){
            updateRecipe.difficulty(recipeDto.getDifficulty());
        }
        if(recipeDto.getDescription() != null){
            updateRecipe.description(recipeDto.getDescription());
        }
        if(recipeDto.getCookingTime() != null){
            updateRecipe.cookingTime(recipeDto.getCookingTime());
        }
        if(recipeDto.getToolName() != null){

            //수정된 toolName db에 저장(중복 제외)
            for(String toolName: recipeDto.getToolName()){
                Tool tool = Tool.builder()
                        .toolName(toolName)
                        .userTools(new ArrayList<>())
                        .recipeTools(new ArrayList<>())
                        .build();
                try{
                    toolRepository.save(tool);
                }catch (DataIntegrityViolationException e){
                    log.trace("이미 존재하는 데이터입니다.");
                }
            }

            //수정된 toolName으로 Tool 탐색 후 RecipeTool에 입력
            List<RecipeTool> recipeTools = new ArrayList<>();
            for(String toolName:recipeDto.getToolName()){
                Tool tool = toolRepository.findByToolName(toolName).get();
                RecipeTool recipeTool = RecipeTool.builder()
                        .tool(tool)
                        .recipe(recipe)
                        .build();
                recipeTools.add(recipeTool);
                tool.addRecipeTool(recipeTool);
            }
            updateRecipe.recipeTools(recipeTools);

        }
        if(recipeDto.getRecipeIngredientDtos() != null){

            //수정된 재료 db에 저장(중복 제외)
            List<RecipeIngredientDto> recipeIngredientDtos = recipeDto.getRecipeIngredientDtos();
            for(RecipeIngredientDto recipeIngredientDto: recipeIngredientDtos){
                Ingredient ingredient = Ingredient.builder()
                        .ingredientName(recipeIngredientDto.getIngredientName())
                        .userIngredients(new ArrayList<>())
                        .recipeIngredients(new ArrayList<>())
                        .build();
                try{
                    ingredientRepository.save(ingredient);
                }catch (DataIntegrityViolationException e){
                    log.trace("이미 존재하는 데이터입니다.");
                }
            }

            //수정된 재료 입력
            List<RecipeIngredient> recipeIngredients = recipeDto.getRecipeIngredientDtos().stream().map(i -> RecipeIngredient
                    .builder()
                    .quantity(i.getQuantity())
                    .recipe(recipe)
                    .ingredient(ingredientRepository.findByIngredientName(i.getIngredientName()).get())
                    .build()).collect(toList());

            //recipeIngredient와 Ingredient의 연관관계 생성
            for(RecipeIngredient recipeIngredient: recipeIngredients){
                Ingredient ingredient = recipeIngredient.getIngredient();
                ingredient.addRecipeIngredient(recipeIngredient);
            }
            updateRecipe.recipeIngredients(recipeIngredients);
        }
        if(recipeDto.getRecipeStepDtos() != null){
            List<RecipeStep> recipeSteps = recipeDto.getRecipeStepDtos().stream().map(s -> RecipeStep
                    .builder()
                    .stepOrder(s.getStepOrder())
                    .content(s.getContent()).recipe(recipe)
                    .build()).collect(toList());
            updateRecipe.recipeSteps(recipeSteps);
        }

        updateRecipe.build();
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
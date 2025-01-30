package org.example.recipe_match_backend.recipe.service;

import jakarta.persistence.EntityManager;
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
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecipeService {

    private final EntityManager entityManager;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;
    private final IngredientRepository ingredientRepository;

    @Transactional
    public Long save(RecipeDto recipeDto,Long userId){

        //재료와 도구 추가
        List<RecipeIngredientDto> recipeIngredientDtos = recipeDto.getRecipeIngredientDtos();
        for(RecipeIngredientDto recipeIngredientDto: recipeIngredientDtos){
            Ingredient ingredient = Ingredient.builder()
                    .ingredientName(recipeIngredientDto.getIngredientName())
                    .recipeIngredients(new ArrayList<>())
                    .userIngredients(new ArrayList<>())
                    .build();
            try{
                ingredientRepository.save(ingredient);
            }catch (DataIntegrityViolationException e){
                log.trace("이미 존재하는 데이터입니다.");
            }
        }
        for(String toolName: recipeDto.getToolName()){
            Tool tool = Tool.builder().toolName(toolName)
                    .recipeTools(new ArrayList<>())
                    .userTools(new ArrayList<>())
                    .build();
            try{
                toolRepository.save(tool);
            }catch (DataIntegrityViolationException e){
                log.trace("이미 존재하는 데이터입니다.");
            }
        }

        Recipe recipe = Recipe.builder().recipeName(recipeDto.getRecipeName()).description(recipeDto.getDescription())
                .cookingTime(recipeDto.getCookingTime()).difficulty(recipeDto.getDifficulty()).category(recipeDto.getCategory())
                .build();//레시피 DTO를 레시피 엔티티로 매핑

        User user = userRepository.findById(userId).get();//사용자 검색

        //유저에 레시피 연관관계 주입
        user.addRecipe(recipe);

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

        Recipe finalRecipe = recipe.toBuilder().recipeSteps(recipeSteps)
                .recipeIngredients(recipeIngredients).recipeTools(recipeTools).user(user)
                .build();
        //마지막으로 레시피 엔티티에 각 엔티티 리스트 주입

        for (RecipeStep step : recipeSteps) {
            entityManager.persist(step);
        }//왜 수동으로 영속화 해야 null값이 안되는거지?

        Recipe saveRecipe = recipeRepository.save(finalRecipe);

        return finalRecipe.getId();
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
                Tool tool = toolRepository.findByToolName(toolName);
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
                    .builder().quantity(i.getQuantity()).recipe(recipe)
                    .ingredient(ingredientRepository.findByIngredientName(i.getIngredientName()))
                    .build()).collect(toList());

            //recipeIngredient와 Ingredient의 연관관계 생성
            for(RecipeIngredient recipeIngredient: recipeIngredients){
                Ingredient ingredient = recipeIngredient.getIngredient();
                ingredient.addRecipeIngredient(recipeIngredient);
            }
            updateRecipe.recipeIngredients(recipeIngredients);
        }
        if(recipeDto.getRecipeStepDtos() != null){
            List<RecipeStep> recipeSteps = recipeDto.getRecipeStepDtos().stream().map(s -> RecipeStep.builder()
                    .stepOrder(s.getStepOrder()).content(s.getContent()).recipe(recipe)
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
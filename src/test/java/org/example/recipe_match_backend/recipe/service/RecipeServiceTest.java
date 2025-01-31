package org.example.recipe_match_backend.recipe.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.recipe_match_backend.domain.recipe.dto.request.RecipeRequest;
import org.example.recipe_match_backend.domain.recipe.dto.RecipeIngredientDto;
import org.example.recipe_match_backend.domain.recipe.dto.RecipeStepDto;
import org.example.recipe_match_backend.domain.recipe.dto.response.RecipeResponse;
import org.example.recipe_match_backend.domain.recipe.service.RecipeService;
import org.example.recipe_match_backend.type.CategoryType;
import org.example.recipe_match_backend.type.DifficultyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void testSave() throws Exception{

        String test = "반복 테스트 1";

        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto("테스트", test);
        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(recipeIngredientDto);

        RecipeStepDto recipeStepDto = new RecipeStepDto(3,test);
        List<RecipeStepDto> recipeStepDtos = new ArrayList<>();
        recipeStepDtos.add(recipeStepDto);

        List<String> toolName = new ArrayList<>();
        toolName.add(test);

        RecipeRequest recipeRequest = RecipeRequest
                .builder()
                .recipeName("테스트3")
                .category(CategoryType.양식)
                .cookingTime(10)
                .description("테스트 입니다")
                .recipeIngredientDtos(recipeIngredientDtos)
                .recipeStepDtos(recipeStepDtos)
                .toolName(toolName)
                .build();

        Long id = recipeService.save(recipeRequest,1L);
    }

    @Test
    public void testFind() throws Exception{
        RecipeResponse findRecipe = recipeService.find(1L);
        assertThat(findRecipe.getRecipeName()).isEqualTo("반복 테스트 1");
        List<RecipeResponse> findAllRecipe = recipeService.findAll();
        assertThat(findAllRecipe.getFirst().getRecipeName()).isEqualTo("반복 테스트 1");
    }

    @Test
    public void testUpdate() throws Exception{
        String test = "수정 테스트";

        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto("수정 테스트", test);
        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(recipeIngredientDto);

        RecipeStepDto recipeStepDto = new RecipeStepDto(123,test);
        List<RecipeStepDto> recipeStepDtos = new ArrayList<>();
        recipeStepDtos.add(recipeStepDto);

        List<String> toolName = new ArrayList<>();
        toolName.add(test);

        RecipeRequest recipeRequest = RecipeRequest
                .builder()
                .recipeName("수정 테스트")
                .category(CategoryType.일식)
                .cookingTime(1234)
                .description("수정 테스트 입니다")
                .recipeIngredientDtos(recipeIngredientDtos)
                .recipeStepDtos(recipeStepDtos)
                .toolName(toolName)
                .build();

        recipeService.update(1L, recipeRequest);

        em.flush();
    }

    @Test
    public void testDelete() throws Exception{
        recipeService.delete(1L);
    }

}

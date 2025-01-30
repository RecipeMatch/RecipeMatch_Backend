package org.example.recipe_match_backend.recipe.service;

import jakarta.transaction.Transactional;
import org.example.recipe_match_backend.recipe.domain.RecipeDto;
import org.example.recipe_match_backend.recipe.domain.RecipeIngredientDto;
import org.example.recipe_match_backend.recipe.domain.RecipeStepDto;
import org.example.recipe_match_backend.type.CategoryType;
import org.example.recipe_match_backend.type.DifficultyType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(value = false)
public class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Test
    public void testSave() throws Exception{

        RecipeIngredientDto recipeIngredientDto = new RecipeIngredientDto("테스트", "반복 테스트15");
        List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();
        recipeIngredientDtos.add(recipeIngredientDto);

        RecipeStepDto recipeStepDto = new RecipeStepDto(3,"테스트15");
        List<RecipeStepDto> recipeStepDtos = new ArrayList<>();
        recipeStepDtos.add(recipeStepDto);

        List<String> toolName = new ArrayList<>();
        toolName.add("반복 테스트15");

        RecipeDto recipeDto = RecipeDto.builder().recipeName("반복 테스트15").category(CategoryType.양식)
                .difficulty(DifficultyType.중간).cookingTime(10).description("테스트 입니다")
                .recipeIngredientDtos(recipeIngredientDtos).recipeStepDtos(recipeStepDtos).toolName(toolName)
                .build();

        Long id = recipeService.save(recipeDto,1L);

        //안되는거: 중복되는 tool,ingredient 처리 안됨, insert 두번씩 됨
    }

    @Test
    public void testFind() throws Exception{
        RecipeDto findRecipe = recipeService.find(35L);
        assertThat(findRecipe.getRecipeName()).isEqualTo("테스트");
        List<RecipeDto> findAllRecipe = recipeService.findAll();
        assertThat(findAllRecipe.getFirst().getRecipeName()).isEqualTo("테스트");
    }

}

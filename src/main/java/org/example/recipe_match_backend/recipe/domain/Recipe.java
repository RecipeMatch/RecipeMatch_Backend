package org.example.recipe_match_backend.recipe.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.global.entity.BaseEntity;
import org.example.recipe_match_backend.searchhistory.domain.SearchHistory;
import org.example.recipe_match_backend.type.CategoryType;
import org.example.recipe_match_backend.type.DifficultyType;
import org.example.recipe_match_backend.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@Entity
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String recipeName;//이름:레시피 입력시 필요한 내용

    @Column(length = 2000)
    private String description;//설명:레시피 입력시 필요한 내용

    private int cookingTime;//시간:레시피 입력시 필요한 내용

    @Enumerated(EnumType.STRING)
    private DifficultyType difficulty;//난이도:레시피 입력시 필요한 내용

    @Enumerated(EnumType.STRING)
    private CategoryType category;//카테고리:레시피 입력시 필요한 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;//사용자:레시피 입력시 필요한 내용

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeStep> recipeSteps = new ArrayList<>();//레시피 단계 및 내용:레시피 입력시 필요한 내용

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();//재료:레시피 입력시 필요한 내용

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeTool> recipeTools = new ArrayList<>();//도구:레시피 입력시 필요한 내용

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeLike> recipeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeBookMark> recipeFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeRating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<SearchHistory> searchHistories = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        recipeIngredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
    }

    public void addRecipeTool(RecipeTool recipeTool) {
        recipeTools.add(recipeTool);
        recipeTool.setRecipe(this);
    }

    public void addRecipeStep(RecipeStep recipeStep) {
        recipeSteps.add(recipeStep);
        recipeStep.setRecipe(this);
    }

}
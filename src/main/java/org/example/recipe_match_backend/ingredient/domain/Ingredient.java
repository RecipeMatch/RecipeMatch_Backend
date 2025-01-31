package org.example.recipe_match_backend.ingredient.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.recipe.domain.RecipeIngredient;
import org.example.recipe_match_backend.recipe.domain.RecipeTool;
import org.example.recipe_match_backend.user.domain.UserIngredient;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ingredientName;

    @OneToMany(mappedBy = "ingredient")
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.PERSIST)
    private List<UserIngredient> userIngredients = new ArrayList<>();

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        this.recipeIngredients.add(recipeIngredient);
    }

}

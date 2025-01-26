package org.example.recipe_match_backend.ingredient.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.recipe_match_backend.recipe.domain.RecipeIngredient;
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

    private String ingredientName;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.PERSIST)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.PERSIST)
    private List<UserIngredient> userIngredients = new ArrayList<>();

}

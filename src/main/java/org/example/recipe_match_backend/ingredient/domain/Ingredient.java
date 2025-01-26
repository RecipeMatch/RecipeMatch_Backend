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

    // 레시피 <-> 재료 관계
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    // User <-> 재료 관계
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<UserIngredient> userIngredients = new ArrayList<>();

}

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

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class Recipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String recipeName;

    @Column(length = 2000)
    private String description;

    private int cookingTime;

    @Enumerated(EnumType.STRING)
    private DifficultyType difficulty;

    @Enumerated(EnumType.STRING)
    private CategoryType category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeStep> recipeSteps = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private List<RecipeTool> recipeTools = new ArrayList<>();

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

}

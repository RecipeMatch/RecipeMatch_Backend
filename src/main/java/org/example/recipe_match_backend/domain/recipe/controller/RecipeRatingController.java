package org.example.recipe_match_backend.domain.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating.RecipeIdRequest;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating.RecipeRatingRequest;
import org.example.recipe_match_backend.domain.recipe.service.RecipeRatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeRatingController {

    private final RecipeRatingService recipeRatingService;

    /**
     * 별점 등록/수정 (Post /api/recipes/{recipeId}/ratings)
     */
    @PostMapping("/{recipeId}/ratings")
    public ResponseEntity<Void> rateRecipe(@PathVariable Long recipeId,
                                           @RequestBody RecipeRatingRequest request){
        recipeRatingService.rateRecipe(recipeId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 특정 레시피의 평균 별점을 조회 (Get /api/recipes/{recipeId}/ratings/average)
     */
    @GetMapping("/{recipeId}/ratings/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long recipeId){
        Double averageRating = recipeRatingService.getAverageRating(recipeId);
        return ResponseEntity.ok(averageRating);
    }
}

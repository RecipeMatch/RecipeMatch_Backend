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
     * 별점 등록/수정
     */
    @PostMapping("/ratings")
    public ResponseEntity<Void> rateRecipe(@RequestBody RecipeRatingRequest request){
        recipeRatingService.rateRecipe(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 특정 레시피의 평균 별점을 조회
     */
    @GetMapping("/ratings/average")
    public ResponseEntity<Double> getAverageRating(@RequestBody RecipeIdRequest request){
        Double averageRating = recipeRatingService.getAverageRating(request);
        return ResponseEntity.ok(averageRating);
    }

}

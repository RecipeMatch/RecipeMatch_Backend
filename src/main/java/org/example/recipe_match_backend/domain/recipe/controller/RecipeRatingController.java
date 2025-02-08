package org.example.recipe_match_backend.domain.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeRating.RecipeAverageRatingRequest;
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
     * POST /api/recipes/{recipeId}/ratings
     * RequestBody에 userId와 ratingValue를 포함
     */
    @PostMapping("/{recipeId}/ratings")
    public ResponseEntity<Void> rateRecipe(@RequestBody RecipeRatingRequest request){
        recipeRatingService.rateRecipe(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 특정 레시피의 평균 별점을 조회
     * GET /api/recipes/{recipeId}/average
     */
    @GetMapping("/{recipeId}/average")
    public ResponseEntity<Double> getAverageRating(@RequestBody RecipeAverageRatingRequest request){
        Double averageRating = recipeRatingService.getAverageRating(request);
        return ResponseEntity.ok(averageRating);
    }

}

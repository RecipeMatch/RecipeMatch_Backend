package org.example.recipe_match_backend.domain.recipe.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipe.RecipeIdAndUserIdRequest;
import org.example.recipe_match_backend.domain.recipe.service.RecipeLikeService;
import org.example.recipe_match_backend.domain.recipe.service.RecipeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipe")
@RequiredArgsConstructor
public class RecipeLikeController {

    private RecipeLikeService recipeLikeService;

    @PostMapping("/like")
    public Long recipeLike(@RequestBody RecipeIdAndUserIdRequest request){
        return recipeLikeService.recipeLike(request.getRecipeId(), request.getUserId());
    }

}

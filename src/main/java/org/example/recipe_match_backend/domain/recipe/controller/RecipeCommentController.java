package org.example.recipe_match_backend.domain.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeComment.RecipeCommentRequest;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeComment.RecipeCommentUpdateRequest;
import org.example.recipe_match_backend.domain.recipe.dto.response.recipeComment.RecipeCommentResponse;
import org.example.recipe_match_backend.domain.recipe.service.RecipeCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeCommentController {

    private final RecipeCommentService recipeCommentService;

    /**
     * 댓글 생성 (POST /api/recipes/comments)
     */
    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody RecipeCommentRequest request) {
        recipeCommentService.createComment(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     *  댓글 수정 (PUT /api/recipes/comments/{commentId})
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<RecipeCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody RecipeCommentUpdateRequest request) {
        RecipeCommentResponse response = recipeCommentService.updateComment(commentId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 삭제 (DELETE /api/recipes/comments/{commentId}?userUid=xxx)
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam String userUid) {
        recipeCommentService.deleteComment(commentId, userUid);
        return ResponseEntity.noContent().build();
    }
}

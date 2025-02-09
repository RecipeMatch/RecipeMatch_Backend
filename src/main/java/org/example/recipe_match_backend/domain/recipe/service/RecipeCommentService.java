package org.example.recipe_match_backend.domain.recipe.service;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.recipe.domain.RecipeComment;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeComment.RecipeCommentRequest;
import org.example.recipe_match_backend.domain.recipe.dto.request.recipeComment.RecipeCommentUpdateRequest;
import org.example.recipe_match_backend.domain.recipe.dto.response.recipeComment.RecipeCommentResponse;
import org.example.recipe_match_backend.domain.recipe.repository.RecipeCommentRepository;
import org.example.recipe_match_backend.domain.recipe.repository.RecipeRepository;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.example.recipe_match_backend.domain.user.repository.UserRepository;
import org.example.recipe_match_backend.global.exception.recipe.RecipeNotFoundException;
import org.example.recipe_match_backend.global.exception.recipeComment.CommandNotFoundException;
import org.example.recipe_match_backend.global.exception.recipeComment.UserNotAuthException;
import org.example.recipe_match_backend.global.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeCommentService {

    private final RecipeRepository recipeRepository;
    private final RecipeCommentRepository recipeCommentRepository;
    private final UserRepository userRepository;

    // 댓글 생성
    @Transactional
    public void createComment(RecipeCommentRequest request) {
        // 레시피 조회
        var recipe = recipeRepository.findById(request.getRecipeId())
                .orElseThrow(RecipeNotFoundException::new);

        // 사용자 조회 (User 엔티티는 고유 식별자(uid)를 가진다고 가정)
        User user = userRepository.findByUid(request.getUserUid())
                .orElseThrow(UserNotFoundException::new);

        // 댓글 생성 (id는 자동생성)
        RecipeComment comment = new RecipeComment(null, user, recipe, request.getContent());
        RecipeComment savedComment = recipeCommentRepository.save(comment);

    }

    // 댓글 수정
    @Transactional
    public RecipeCommentResponse updateComment(Long commentId, RecipeCommentUpdateRequest request) {
        RecipeComment comment = recipeCommentRepository.findById(commentId)
                .orElseThrow(CommandNotFoundException::new);

        // 댓글 작성자와 요청 사용자가 일치하는지 확인
        if (!comment.getUser().getUid().equals(request.getUserUid())) {
            throw new RuntimeException("댓글 수정 권한이 없습니다.");
        }

        // 댓글 내용 수정
        comment.updateContent(request.getContent());

        return RecipeCommentResponse.builder()
                .id(comment.getId())
                .userUid(comment.getUser().getUid())
                .recipeId(comment.getRecipe().getId())
                .content(comment.getContent())
                .build();
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String userUId) {
        RecipeComment comment = recipeCommentRepository.findById(commentId)
                .orElseThrow(CommandNotFoundException::new);

        // 댓글 작성자와 요청 사용자가 일치하는지 확인
        if (!comment.getUser().getUid().equals(userUId)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        recipeCommentRepository.delete(comment);
    }

}

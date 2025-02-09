package org.example.recipe_match_backend.domain.recipe.repository;

import org.example.recipe_match_backend.domain.recipe.domain.RecipeComment;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 레시피 댓글 관련 리포지토리
 */
public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {


}

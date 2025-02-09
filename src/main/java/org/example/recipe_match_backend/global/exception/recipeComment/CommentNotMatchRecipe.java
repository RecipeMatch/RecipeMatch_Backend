package org.example.recipe_match_backend.global.exception.recipeComment;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class CommentNotMatchRecipe extends BusinessException {
    public CommentNotMatchRecipe() {
        super(ErrorCode.COMMENT_NOT_MATCH_RECIPE);
    }
}

package org.example.recipe_match_backend.global.exception.recipeComment;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class CommentNotFoundException extends BusinessException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}

package org.example.recipe_match_backend.global.exception.recipeComment;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class UserNotAuthException extends BusinessException {
    public UserNotAuthException() {
        super(ErrorCode.USER_NOT_AUTH);
    }
}

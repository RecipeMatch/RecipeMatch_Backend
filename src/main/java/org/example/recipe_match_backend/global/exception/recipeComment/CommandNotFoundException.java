package org.example.recipe_match_backend.global.exception.recipeComment;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class CommandNotFoundException extends BusinessException {
    public CommandNotFoundException() {
        super(ErrorCode.COMMAND_NOT_FOUND);
    }
}

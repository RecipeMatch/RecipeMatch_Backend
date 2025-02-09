package org.example.recipe_match_backend.global.exception.recipe;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class RecipeNotFoundException extends BusinessException {
    public RecipeNotFoundException() {
        super(ErrorCode.RECIPE_NOT_FOUND);
    }
}

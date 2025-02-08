package org.example.recipe_match_backend.global.exception.recipeRating;

import org.example.recipe_match_backend.global.exception.BusinessException;
import org.example.recipe_match_backend.global.exception.ErrorCode;

public class InvalidRatingValueException extends BusinessException {
    public InvalidRatingValueException() {
        super(ErrorCode.INVALID_RATING_VALUE);
    }
}

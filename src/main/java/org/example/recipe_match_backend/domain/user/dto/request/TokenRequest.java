package org.example.recipe_match_backend.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRequest {

    private String refreshToken;

}

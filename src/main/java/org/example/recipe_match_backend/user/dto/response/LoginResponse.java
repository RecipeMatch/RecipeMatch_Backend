package org.example.recipe_match_backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String googleUid;
    private String email;
    private String accessToken;
    private String refreshToken;
}

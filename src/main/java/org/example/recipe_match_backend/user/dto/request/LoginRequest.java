package org.example.recipe_match_backend.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    // RN 앱에서 받은 Firebase ID Token
    private String idToken;
}

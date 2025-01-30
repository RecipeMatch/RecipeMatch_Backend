package org.example.recipe_match_backend.domain.user.controller;

import org.example.recipe_match_backend.domain.user.dto.request.OAuthRequest;
import org.example.recipe_match_backend.domain.user.dto.request.RefreshRequest;
import org.example.recipe_match_backend.domain.user.dto.response.TokenResponse;
import org.example.recipe_match_backend.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 로그인, IdToken 및 name 전달 받음
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> userLogin(@RequestBody OAuthRequest request) throws Exception {
        return ResponseEntity.ok(userService.userLogin(request));
    }

    // accessToken 재발급 (프론트로부터 refreshToken 전달 받음)
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> recreateToken(@RequestBody RefreshRequest refreshRequest){
        return ResponseEntity.ok(userService.recreateToken(refreshRequest));
    }


}

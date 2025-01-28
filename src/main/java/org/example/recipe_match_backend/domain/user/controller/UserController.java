package org.example.recipe_match_backend.domain.user.controller;

import org.example.recipe_match_backend.domain.user.dto.request.OAuthRequest;
import org.example.recipe_match_backend.domain.user.dto.request.TokenRequest;
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

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> userLogin(@RequestBody OAuthRequest request) {
        return userService.userLogin(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> recreateToken(@RequestBody TokenRequest tokenRequest){
        return userService.recreateToken(tokenRequest);
    }





}

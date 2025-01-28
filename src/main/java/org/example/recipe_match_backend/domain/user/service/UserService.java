package org.example.recipe_match_backend.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.recipe_match_backend.domain.user.domain.User;
import org.example.recipe_match_backend.domain.user.dto.request.OAuthRequest;
import org.example.recipe_match_backend.domain.user.dto.request.TokenRequest;
import org.example.recipe_match_backend.domain.user.dto.response.TokenResponse;
import org.example.recipe_match_backend.global.exception.login.InvalidToken;
import org.example.recipe_match_backend.global.exception.login.UserNotFoundException;
import org.example.recipe_match_backend.global.jwt.JwtTokenProvider;
import org.example.recipe_match_backend.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseEntity<TokenResponse> userLogin(OAuthRequest request) {
        Optional<User> users = userRepository.findByGoogleUid(request.getUid());

        if(users.isPresent()){
            User user = users.get();
            return ExistingMemberLogin(user);
        } else {
            return NewMemberLogin(request);
        }
    }

    private ResponseEntity<TokenResponse> ExistingMemberLogin(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user.getGoogleUid());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getGoogleUid());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    private ResponseEntity<TokenResponse> NewMemberLogin(OAuthRequest request) {
        User user = User.builder()
                .googleUid(request.getUid())
                .build();

        userRepository.save(user);

        String accessToken = jwtTokenProvider.createAccessToken(user.getGoogleUid());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getGoogleUid());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }

    @Transactional
    public ResponseEntity<TokenResponse> recreateToken(TokenRequest tokenRequest) {

        if(!jwtTokenProvider.validateRefreshToken(tokenRequest.getRefreshToken())){
            throw new InvalidToken();
        }
        String uid = jwtTokenProvider.getUid(tokenRequest.getRefreshToken());

        User user = userRepository.findByGoogleUid(uid)
                .orElseThrow(UserNotFoundException::new);

        String accessToken = jwtTokenProvider.createAccessToken(user.getGoogleUid());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getGoogleUid());

        return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
    }
}

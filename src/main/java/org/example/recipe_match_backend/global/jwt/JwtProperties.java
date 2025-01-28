package org.example.recipe_match_backend.global.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secretKey;

    private final Long accessToken = 3 * 60 * 60 * 1000L;         // 3시간

    private final Long refreshToken = 14 * 24 * 60 * 60 * 1000L;  // 2주

}

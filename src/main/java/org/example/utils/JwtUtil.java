package org.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class JwtUtil {

    private static final String SECRET = "secret";
    private static final long EXPIRATION_TIME = 900_000_000;

    // Create token
    public static String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    // Verify JWT token
    public static DecodedJWT verifyToken(String token) throws Exception {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
    }

    // Get User ID
    public static String getJWTUserId (String token) throws Exception {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
        return jwt.getSubject();
    }
}

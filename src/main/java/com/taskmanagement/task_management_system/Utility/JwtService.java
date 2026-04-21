package com.taskmanagement.task_management_system.Utility;

import com.taskmanagement.task_management_system.Exception.Token.ExpiredTokenException;
import com.taskmanagement.task_management_system.Exception.Token.InvalidTokenException;
import com.taskmanagement.task_management_system.Model.dto.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtService {

    @Value("${jwt.expiration.access-token}")
    private String expiration;

    private final SecretKey secretKey = Keys.hmacShaKeyFor("SECRET-KEY-6543wy5rereO68543354EW4577HO9".getBytes());  // Avoid hardcoding


    /**
     * Creates a JWT token with the provided claims and subject.
     *
     * @param claims  Claims to be included in the token
     * @param subject Subject (user ID)
     * @return JWT token as String
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + getExpirationAsLong()))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Generates a JWT token for the user.
     *
     * @param userInfo User info object
     * @return JWT token as String
     */
    // ONLY show changed parts

    public String generateToken(UserInfo userInfo) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userInfo.getEmail());
        claims.put("roles", userInfo.getRole().name());

        return createToken(claims, userInfo.getUsername());
    }


    /**
     * Converts expiration time from String to long.
     * Throws IllegalArgumentException if conversion fails.
     *
     * @return Expiration time in milliseconds
     */
    public long getExpirationAsLong() {
        try {
            return Long.parseLong(expiration);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid JWT expiration value: " + expiration, e);
        }
    }


    /**
     * Extracts all claims from the JWT token.
     *
     * @param token JWT token
     * @return Claims object containing all JWT claims
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredTokenException("JWT Token has expired: " + ex.getMessage());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid JWT token: " + ex.getMessage());
        }
    }

    /**
     * A generic method to extract a specific claim from the JWT token.
     *
     * @param token          JWT token
     * @param claimsResolver Lambda to resolve a claim
     * @param <T>            Type of claim to return
     * @return The claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts expiration date from the JWT token.
     *
     * @param token JWT token
     * @return Expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts username from the JWT token.
     *
     * @param token JWT token
     * @return Username
     */
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidTokenException("Invalid or expired token: " + ex.getMessage());
        }
    }

    /**
     * Extracts email from the JWT token.
     *
     * @param token JWT token
     * @return Email address
     */
    public String extractEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    /**
     * Extracts roles from the JWT token.
     *
     * @param token JWT token
     * @return Roles as a comma-separated string
     */
    public String extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token JWT token
     * @return True if the token is expired, otherwise false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the token from the Authorization header.
     *
     * @param authHeader Authorization header
     * @return Token string
     */
    public String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);  // Remove "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Invalid Authorization header format");
        }
    }

    /**
     * Validates the JWT token by checking the username and expiration.
     *
     * @param token       JWT token
     * @param email       Username to match
     * @param userDetails User details object
     * @return True if token is valid, otherwise false
     */
    public boolean validateToken(String token, String email, UserDetails userDetails) {
        try {
            boolean isSameUser = email.equals(userDetails.getUsername());
            boolean isNotExpired = !isTokenExpired(token);
            return isSameUser && isNotExpired;
        } catch (ExpiredTokenException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidTokenException("Invalid Token: " + ex.getMessage());
        }
    }

}

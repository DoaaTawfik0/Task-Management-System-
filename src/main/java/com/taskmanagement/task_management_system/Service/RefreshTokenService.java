package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Exception.Token.InvalidTokenException;
import com.taskmanagement.task_management_system.Model.entity.RefreshToken;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional()
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repo;

    @Value("${jwt.expiration.refresh-token}")
    private int EXPIRATION;


    public RefreshToken create(Users user) {

        // delete old
        repo.deleteByUser(user);
        repo.flush();

        System.out.println(repo.findByUser(user));

        return repo.save(
                RefreshToken.builder()
                        .user(user)
                        .token(UUID.randomUUID().toString())
                        .expiryDate(LocalDateTime.now().plusDays(EXPIRATION))
                        .revoked(false)
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public RefreshToken verify(String token) {

        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (rt.isRevoked() || rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh token expired or revoked");
        }
        return rt;
    }

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repo.save(token);
    }
}
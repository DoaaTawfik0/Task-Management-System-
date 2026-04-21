package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Exception.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Token.InvalidCredentialsException;
import com.taskmanagement.task_management_system.Model.dto.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.RefreshToken;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final RefreshTokenService refreshService;

    public Users register(UserInfo dto) {
        if (repo.existsByEmail(dto.getEmail()))
            throw new ResourceAlreadyExistException("User exists");

        dto.setPassword(encoder.encode(dto.getPassword()));

        return repo.save(createNewUser(dto));
    }

    public AuthResponse login(UserInfo dto) {

        Users user = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials: email is incorrect "));

        if (!encoder.matches(dto.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid credentials: password is incorrect");

        dto.setRole(user.getRole());

        String access = jwt.generateToken(dto);
        String refresh = refreshService.create(user).getToken();

        return new AuthResponse(access, refresh);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {

        RefreshToken rt = refreshService.verify(request.refreshToken());
        Users user = rt.getUser();

        UserInfo dto = new UserInfo(
                user.getUsername(),
                user.getEmail(),
                null,
                user.getRole()
        );

        String newAccess = jwt.generateToken(dto);

        return new AuthResponse(newAccess, rt.getToken());
    }

    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshService.verify(request.refreshToken());

        refreshService.revoke(refreshToken);
    }

    private static Users createNewUser(UserInfo dto) {
        return Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }
}
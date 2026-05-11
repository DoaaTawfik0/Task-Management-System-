package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Exception.Token.InvalidCredentialsException;
import com.taskmanagement.task_management_system.Mapper.UserMapper;
import com.taskmanagement.task_management_system.Model.dto.auth.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.auth.LoginRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UpdateUserRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.RefreshToken;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService extends BaseService<Users, Long> {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final RefreshTokenService refreshService;
    private final UserMapper mapper;

    @Override
    protected BaseRepository<Users, Long> getRepository() {
        return repo;
    }

    public UserInfo register(RegisterRequest dto) {
        if (repo.existsByEmail(dto.getEmail()))
            throw new ResourceAlreadyExistException("User exists");

        dto.setPassword(encoder.encode(dto.getPassword()));

        Users saved = repo.save(mapper.RegisterToUser(dto));
        return mapper.entityToDto(saved);
    }

    public AuthResponse login(LoginRequest dto) {

        Users user = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials: email is incorrect "));

        if (!encoder.matches(dto.getPassword(), user.getPassword()))
            throw new InvalidCredentialsException("Invalid credentials: password is incorrect");

        dto.setRole(user.getRole());

        UserInfo userInfo = mapper.entityToDto(user);


        String access = jwt.generateToken(userInfo);
        String refresh = refreshService.create(user).getToken();

        return new AuthResponse(access, refresh);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {

        RefreshToken rt = refreshService.verify(request.refreshToken());
        Users user = rt.getUser();

        UserInfo dto = mapper.entityToDto(user);
        ;

        String newAccess = jwt.generateToken(dto);

        return new AuthResponse(newAccess, rt.getToken());
    }

    public void logout(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshService.verify(request.refreshToken());
        refreshToken.setRevoked(true);
    }

    private static Users createNewUser(RegisterRequest dto) {
        return Users.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .build();
    }

    public UserInfo getCurrentUser(Users user) {
        return mapper.entityToDto(user);
    }

    public UserInfo updateCurrentUser(Users user, UpdateUserRequest request) {
        mapper.updateUserFromDto(request, user);
        super.save(user);
        return mapper.entityToDto(user);
    }

    public UserInfo getUserById(Long id) {
        Users user = getUserEntity(id);
        return mapper.entityToDto(user);
    }

    public void deleteUserById(Long id) {
        super.delete(id, Users.class.getSimpleName());
    }

    public Users getUserEntity(Long id) {
        return super.findById(id, User.class.getSimpleName());
    }

    public Users findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
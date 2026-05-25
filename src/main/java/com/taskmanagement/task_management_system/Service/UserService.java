package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Enum.AuthProvider;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Exception.Token.InvalidCredentialsException;
import com.taskmanagement.task_management_system.Mapper.UserMapper;
import com.taskmanagement.task_management_system.Model.dto.auth.AuthResponse;
import com.taskmanagement.task_management_system.Model.dto.auth.LoginRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RefreshTokenRequest;
import com.taskmanagement.task_management_system.Model.dto.auth.RegisterRequest;
import com.taskmanagement.task_management_system.Model.dto.user.ChangePasswordRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UpdateUserRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.RefreshToken;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Users user = mapper.RegisterToUser(dto);

        user.setProvider(AuthProvider.LOCAL);

        Users saved = repo.save(user);
        return mapper.entityToDto(saved);
    }

    public AuthResponse login(LoginRequest dto) {

        Users user = repo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials: email is incorrect "));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new InvalidCredentialsException(
                    "This account uses OAuth2 login with " + user.getProvider()
            );
        }

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

    @Transactional(readOnly = true)
    public UserInfo getCurrentUser(Long userId) {
        Users user = getUserEntity(userId);

        return mapper.entityToDto(user);
    }

    public UserInfo updateCurrentUser(Long userId, UpdateUserRequest request) {
        Users user = getUserEntity(userId);
        mapper.updateUserFromDto(request, user);
//        super.save(user);
        return mapper.entityToDto(user);
    }

    public UserInfo getUserById(Long id) {
        Users user = getUserEntity(id);
        return mapper.entityToDto(user);
    }

    public void deleteUserById(Long id) {
        super.delete(id, Users.class.getSimpleName());
    }

    @Transactional(readOnly = true)
    public Users getUserEntity(Long id) {
        return super.findById(id, Users.class.getSimpleName());
    }

    @Transactional(readOnly = true)
    public Users findByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public Page<UserInfo> getUsers(Pageable pageable) {
        return repo.findAllUsers(pageable);
    }

    @Transactional(readOnly = true)
    public List<UserInfo> searchUsersBy(String keyword) {
        return repo.searchUsersByKeyword(keyword.trim());
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {

        Users user = getUserEntity(userId);

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
    }
}
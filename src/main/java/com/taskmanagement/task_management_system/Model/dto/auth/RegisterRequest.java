package com.taskmanagement.task_management_system.Model.dto.auth;

import com.taskmanagement.task_management_system.Enum.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class RegisterRequest {
    @NotBlank
    @Size(min = 6, max = 10)
    private String username;

    @NotBlank
    @Size(min = 3)
    private String firstName;
    @NotBlank
    @Size(min = 3)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    private UserRole role = UserRole.USER;
}

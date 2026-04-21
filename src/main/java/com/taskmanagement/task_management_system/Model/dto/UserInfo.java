package com.taskmanagement.task_management_system.Model.dto;

import com.taskmanagement.task_management_system.Enum.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Size(min = 3, max = 50)
    @NotBlank
    private String username;

    @Email
    @NotBlank()
    private String email;

    @NotBlank
    private String password;

    private UserRole role = UserRole.USER;
}

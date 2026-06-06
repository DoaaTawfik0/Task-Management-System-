package com.taskmanagement.task_management_system.Model.dto.user;

import com.taskmanagement.task_management_system.Enum.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    @Size(min = 3)
    private String firstName;
    @Size(min = 3)
    private String lastName;
    @Email
    private String email;
    private String password;
    private UserRole role;
}

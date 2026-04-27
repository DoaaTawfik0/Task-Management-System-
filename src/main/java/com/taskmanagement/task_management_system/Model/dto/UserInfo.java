package com.taskmanagement.task_management_system.Model.dto;

import com.taskmanagement.task_management_system.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String username;

    private String fullName;

    private String email;

    private UserRole role;
}

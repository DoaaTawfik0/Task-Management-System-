package com.taskmanagement.task_management_system.Model.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRegistrationRequest {

    private String registrationToken;

    private String email;
}

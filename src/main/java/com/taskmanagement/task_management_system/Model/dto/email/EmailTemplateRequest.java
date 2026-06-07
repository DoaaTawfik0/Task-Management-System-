package com.taskmanagement.task_management_system.Model.dto.email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String content;

    @NotBlank
    private String subject;

    @NotBlank
    private String name;
}

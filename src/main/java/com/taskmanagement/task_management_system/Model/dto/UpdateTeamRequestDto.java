package com.taskmanagement.task_management_system.Model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTeamRequestDto {
    private String name;
    private String description;
}

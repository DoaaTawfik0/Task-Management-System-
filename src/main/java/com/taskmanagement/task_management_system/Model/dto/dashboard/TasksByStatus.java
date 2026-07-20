package com.taskmanagement.task_management_system.Model.dto.dashboard;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksByStatus {
    private Long completed;
    private Long inProgress;
    private Long todo;
}

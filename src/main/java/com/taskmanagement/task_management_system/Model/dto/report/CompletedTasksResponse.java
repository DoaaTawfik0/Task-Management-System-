package com.taskmanagement.task_management_system.Model.dto.report;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompletedTasksResponse {

    private long totalCompletedTasks;

    private List<CompletedTaskReport> tasks;
}
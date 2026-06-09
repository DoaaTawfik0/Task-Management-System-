package com.taskmanagement.task_management_system.Model.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OverdueTasksResponse {

    private long totalOverdueTasks;

    private List<OverdueTaskReport> tasks;
}
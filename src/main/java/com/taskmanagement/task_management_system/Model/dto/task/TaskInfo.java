package com.taskmanagement.task_management_system.Model.dto.task;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Model.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TaskInfo {
    private String title;

    private String description;

    private Priority priority;

    private Status status;

    private LocalDateTime dueDate;

    public TaskInfo(Task task) {
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.status = task.getStatus();
        this.dueDate = task.getDueDate();
    }
}

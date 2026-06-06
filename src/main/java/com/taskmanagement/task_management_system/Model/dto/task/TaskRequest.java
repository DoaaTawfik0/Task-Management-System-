package com.taskmanagement.task_management_system.Model.dto.task;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    @NotBlank
    @Size(min = 5, max = 50)
    private String title;

    @NotBlank
    @Size(min = 10, max = 250)
    private String description;

    // default priority to be set is MEDIUM
    private Priority priority = Priority.MEDIUM;

    // default status to be set is To_Do
    private Status status = Status.TO_DO;

    // default status to be set is NOW
    @FutureOrPresent
    private LocalDateTime dueDate = LocalDateTime.now();
}

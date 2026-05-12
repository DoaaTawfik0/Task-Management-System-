package com.taskmanagement.task_management_system.Repository.task;

import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface TaskRepositoryCustom {
    List<TaskInfo> findAllTasks(Specification<Task> spec);
}

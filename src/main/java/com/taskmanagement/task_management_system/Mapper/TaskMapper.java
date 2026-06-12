package com.taskmanagement.task_management_system.Mapper;

import com.taskmanagement.task_management_system.Model.dto.dashboard.UpcomingDeadlines;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.task.TaskRequest;
import com.taskmanagement.task_management_system.Model.dto.task.UpdateTaskRequest;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskRequest dto);

    TaskInfo toDto(Task entity);


    List<TaskInfo> toDtos(List<Task> entities);

    List<UpcomingDeadlines> toUpcomingDeadlines(List<Task>entities);


    // Update: Ignore null fields to preserve existing values
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskFromDto(UpdateTaskRequest dto, @MappingTarget Task entity);
}

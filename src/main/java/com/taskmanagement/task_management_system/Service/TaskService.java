package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Mapper.TaskMapper;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.task.TaskRequest;
import com.taskmanagement.task_management_system.Model.dto.task.UpdateTaskRequest;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService extends BaseService<Task, Long> {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    @Override
    protected BaseRepository<Task, Long> getRepository() {
        return taskRepository;
    }


    public TaskInfo addTask(TaskRequest request) {
        Task task = mapper.toEntity(request);
        String title = request.getTitle();

        checkTitleNotExist(title);

        Task saved = super.save(task);

        return mapper.toDto(saved);
    }


    @Transactional(readOnly = true)
    public List<TaskInfo> getAllTasks() {
        List<Task> savedTasks = super.findAll();

        return mapper.toDtos(savedTasks);
    }

    @Transactional(readOnly = true)
    public TaskInfo getTaskById(Long id) {
        Task task = super.findById(id, Task.class.getSimpleName());
        return mapper.toDto(task);
    }

    public TaskInfo updateTaskById(Long id, UpdateTaskRequest request) {
        Task task = super.findById(id, Task.class.getSimpleName());
        // check if updated title already exist
        checkTitleNotExist(request.getTitle());

        mapper.updateTaskFromDto(request, task);
        super.save(task);
        return mapper.toDto(task);
    }

    public void deleteTaskById(Long id) {
        super.delete(id, Task.class.getSimpleName());
    }

    private void checkTitleNotExist(String title) {
        if (title != null && taskRepository.existsByTitle(title)) {
            throw new ResourceAlreadyExistException("Task already exist with title: " + title);
        }
    }
}

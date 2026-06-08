package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TaskMapper;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.task.TaskRequest;
import com.taskmanagement.task_management_system.Model.dto.task.UpdateTaskRequest;
import com.taskmanagement.task_management_system.Model.dto.user.UserData;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.specification.TaskSpecification;
import com.taskmanagement.task_management_system.Repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService extends BaseService<Task, Long> {

    private final TaskRepository taskRepository;
    private final TeamRepository teamRepository;
    private final UserService userService;
    private final CommentService commentService;
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
        return taskRepository.findTaskById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
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

    public void assignUser(Long taskId, Long userId) {

        Task task = getTaskEntity(taskId);
        Users user = userService.getUserEntity(userId);

        user.assignTask(task);
        super.save(task);
    }

    public void assignUsers(Long taskId, List<Long> userIds) {
        Task task = getTaskEntity(taskId);

        for (Long userId : userIds) {
            Users user = userService.getUserEntity(userId);
            user.assignTask(task);
        }

        super.save(task);
    }

    public TaskInfo updatePriority(Long taskId, Priority priority) {
        Task task = super.findById(taskId, Task.class.getSimpleName());

        task.setPriority(priority);

        super.save(task);

        return mapper.toDto(task);
    }

    public TaskInfo updateStatus(Long taskId, Status status) {
        Task task = super.findById(taskId, Task.class.getSimpleName());

        task.setStatus(status);

        super.save(task);

        return mapper.toDto(task);
    }

    public List<TaskInfo> getTasks(Status status, Priority priority, Long userId) {

        Specification<Task> spec = Specification.unrestricted();

        if (status != null) {
            spec = spec.and(TaskSpecification.hasStatus(status));
        }

        if (priority != null) {
            spec = spec.and(TaskSpecification.hasPriority(priority));
        }

        if (userId != null) {
            spec = spec.and(TaskSpecification.assignedToUser(userId));
        }

        return taskRepository.findAllTasks(spec);
    }

    @Transactional(readOnly = true)
    public Task getTaskEntity(Long id) {
        return super.findById(id, Task.class.getSimpleName());
    }

    public void unassignUser(Long taskId, Long userId) {
        taskRepository.unassignUser(taskId, List.of(userId));
    }

    public void unassignUsers(Long taskId, List<Long> userIds) {
        taskRepository.unassignUser(taskId, userIds);
    }

    public Page<UserData> getAssignedUsers(Long taskId, Pageable pageable) {
        return taskRepository.getAssignedUsers(taskId, pageable);
    }

    public List<TaskInfo> getMyTasks(Long userId) {
        return taskRepository.getMyTasks(userId);
    }

    public TaskInfo assignTaskToTeam(Long taskId, Long teamId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        task.setTeam(team);

        taskRepository.save(task);

        return mapper.toDto(task);
    }
}

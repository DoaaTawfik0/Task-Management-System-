package com.taskmanagement.task_management_system.Startup;


import com.taskmanagement.task_management_system.Enum.AuthProvider;
import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Repository.task.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeUsers();
        initializeTeams();
        initializeTasks();
    }

    private void initializeUsers() {

        if (userRepository.count() > 0) {
            return;
        }

        Users admin = Users.builder()
                .username("admin")
                .firstName("System")
                .lastName("Admin")
                .email("admin@taskmanagement.com")
                .password(passwordEncoder.encode("Admin@123"))
                .role(UserRole.ADMIN)
                .provider(AuthProvider.LOCAL)
                .build();

        Users user1 = Users.builder()
                .username("user1")
                .firstName("Normal")
                .lastName("User")
                .email("user1@taskmanagement.com")
                .password(passwordEncoder.encode("User@123"))
                .role(UserRole.USER)
                .provider(AuthProvider.LOCAL)
                .build();

        Users user2 = Users.builder()
                .username("user2")
                .firstName("Normal")
                .lastName("User")
                .email("user2@taskmanagement.com")
                .password(passwordEncoder.encode("User@123"))
                .role(UserRole.USER)
                .provider(AuthProvider.LOCAL)
                .build();

        userRepository.saveAll(List.of(admin, user1,user2));
    }

    private void initializeTeams() {

        if (teamRepository.count() > 0) {
            return;
        }

        Users admin = userRepository.findByUsername("admin")
                .orElseThrow();

        Users user = userRepository.findByUsername("user1")
                .orElseThrow();

        Team team = Team.builder()
                .name("Backend Team")
                .description("Responsible for backend development")
                .build();

        team.addUser(admin);
        team.addUser(user);

        teamRepository.save(team);
    }

    private void initializeTasks() {

        if (taskRepository.count() > 0) {
            return;
        }

        Team team = teamRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        Users admin = userRepository.findByUsername("admin")
                .orElseThrow();

        Users user = userRepository.findByUsername("user1")
                .orElseThrow();

        Task task1 = Task.builder()
                .title("Setup Spring Security")
                .description("Implement JWT authentication")
                .priority(Priority.HIGH)
                .status(Status.IN_PROGRESS)
                .dueDate(LocalDateTime.now().plusDays(5))
                .team(team)
                .build();

        task1.addUser(admin);

        Task task2 = Task.builder()
                .title("Implement Reports")
                .description("Create completed and overdue reports")
                .priority(Priority.MEDIUM)
                .status(Status.TO_DO)
                .dueDate(LocalDateTime.now().plusDays(10))
                .team(team)
                .build();

        task2.addUser(user);

        taskRepository.saveAll(List.of(task1, task2));
    }
}
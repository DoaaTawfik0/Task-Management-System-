package com.taskmanagement.task_management_system.Repository.task;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Model.dto.dashboard.TaskByPriority;
import com.taskmanagement.task_management_system.Model.dto.report.CompletedTaskReport;
import com.taskmanagement.task_management_system.Model.dto.report.OverdueTaskReport;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.dto.user.UserData;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long>,
        JpaSpecificationExecutor<Task>,
        TaskRepositoryCustom {
    Boolean existsByTitle(String title);


    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.task.TaskInfo(
                 t.title,
                 t.description,
                 t.priority,
                 t.status,
                 t.dueDate
            )
            FROM Task t
            WHERE t.id = :id
            """)
    Optional<TaskInfo> findTaskById(@Param("id") Long id);

    @Modifying
    @Query(value = """
            DELETE FROM task_users
            WHERE task_id = :taskId
            AND user_id IN (:userIds)
            """, nativeQuery = true)
    void unassignUser(@Param("taskId") Long taskId,
                      @Param("userIds") List<Long> userIds);


    @Query("""
                SELECT new com.taskmanagement.task_management_system.Model.dto.user.UserData(
                    u.username,
                    CONCAT(u.firstName, ' ', u.lastName),
                    u.email
                )
                FROM Users u
                JOIN u.tasks t
                WHERE t.id = :taskId
            """)
    Page<UserData> getAssignedUsers(@Param("taskId") Long taskId, Pageable pageable);


    @Query("""
                SELECT new com.taskmanagement.task_management_system.Model.dto.task.TaskInfo(
                    t.title,
                    t.description,
                    t.priority,
                    t.status,
                    t.dueDate
                )
                FROM Task t
                JOIN t.users u
                WHERE u.id = :userId
            """)
    List<TaskInfo> getMyTasks(Long userId);

    @Query("""
    SELECT DISTINCT new com.taskmanagement.task_management_system.Model.dto.report.OverdueTaskReport(
        t.id,
        t.title,
        t.description,
        t.priority,
        t.status,
        t.dueDate,
        u.id,
        u.firstName
    )
    FROM Task t
    LEFT JOIN t.users u
    WHERE t.dueDate < :now
    AND t.status <> com.taskmanagement.task_management_system.Enum.Status.COMPLETED
""")
    List<OverdueTaskReport> findOverdueTasks(@Param("now") LocalDateTime now);

    @Query("""
                SELECT new com.taskmanagement.task_management_system.Model.dto.report.OverdueTaskReport(
                    t.id,
                    t.title,
                    t.description,
                    t.priority,
                    t.status,
                    t.dueDate,
                    u.id,
                    u.firstName
                )
                FROM Task t
                JOIN t.users u
                WHERE u.id = :userId
                  AND t.dueDate < :now
                  AND t.status <> com.taskmanagement.task_management_system.Enum.Status.COMPLETED
            """)
    List<OverdueTaskReport> findUserOverdueTasks(
            @Param("userId") Long userId,
            @Param("now") LocalDateTime now
    );


    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.report.CompletedTaskReport(
                t.id,
                t.title,
                t.updatedAt,
                u.id,
                u.firstName
            )
            FROM Task t
            JOIN t.users u
            WHERE t.status = com.taskmanagement.task_management_system.Enum.Status.COMPLETED
            """)
    List<CompletedTaskReport> findCompletedTasks();

    List<Task> findTasksByDueDate(LocalDateTime day);


    @Query("""
        SELECT COUNT(DISTINCT t)
        FROM Task t
        JOIN t.users u
        WHERE u.id = :userId
        AND t.status = :status
        """)
    Long countTasks(@Param("userId") Long userId, @Param("status") Status status);

    @Query("""
        SELECT new com.taskmanagement.task_management_system.Model.dto.dashboard.TaskByPriority(
                 t.id,
                 t.title,
                 t.priority,
                 t.dueDate
        )
        FROM Task t
        JOIN t.users u
        WHERE u.id = :userId
        AND t.priority =  :priority
        """)
    List<TaskByPriority> findByUserIdAndPriority(@Param("userId") Long userId , @Param("priority") Priority priority);


    List<TaskInfo> findTop5ByUsersIdAndStatusOrderByIdDesc(Long userId , Status status);


    List<Task> findByUsersIdAndDueDateAfter(Long userId, LocalDateTime now);

}

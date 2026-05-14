package com.taskmanagement.task_management_system.Repository.task;

import com.taskmanagement.task_management_system.Base.BaseRepository;
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
}

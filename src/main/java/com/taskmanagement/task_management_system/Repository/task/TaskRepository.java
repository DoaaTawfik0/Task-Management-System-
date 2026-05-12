package com.taskmanagement.task_management_system.Repository.task;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}

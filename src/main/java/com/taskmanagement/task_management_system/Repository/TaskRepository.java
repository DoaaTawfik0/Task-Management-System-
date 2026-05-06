package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends BaseRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {
    Boolean existsByTitle(String title);
}

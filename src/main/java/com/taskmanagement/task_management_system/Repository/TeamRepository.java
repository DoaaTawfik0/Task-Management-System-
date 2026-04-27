package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.entity.Team;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends BaseRepository<Team , Long> {
    boolean existsById(Long id);
}

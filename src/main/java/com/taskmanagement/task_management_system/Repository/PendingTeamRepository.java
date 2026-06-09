package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Model.entity.PendingJoiningTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingTeamRepository extends JpaRepository<PendingJoiningTeam , Long> {

    Boolean existsByUserIdAndTeamId(Long userId , Long teamId);
}

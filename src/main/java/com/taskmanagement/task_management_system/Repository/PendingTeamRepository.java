package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Model.entity.PendingJoiningTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PendingTeamRepository extends JpaRepository<PendingJoiningTeam , Long> {

    Boolean existsByUserIdAndTeamId(Long userId , Long teamId);

    @Query(
            """
            SELECT
            COUNT(p)
            FROM PendingJoiningTeam p
            WHERE p.userId = :userId
            """
    )
    Long CountAllByUserId(@Param("userId") Long userId);
}

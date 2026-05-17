package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.team.TeamAvailableUsers;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Model.dto.team.TeamMembersCountResponse;
import com.taskmanagement.task_management_system.Model.entity.Team;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends BaseRepository<@NonNull Team, @NonNull Long> {
    @Query("""
                   SELECT new com.taskmanagement.task_management_system.Model.dto.team.TeamInfo(
                        t.name,
                        t.description
                   )
                   FROM Team t
            """)
    List<TeamInfo> findAllTeams();

    @Query("""
                   SELECT new com.taskmanagement.task_management_system.Model.dto.team.TeamInfo(
                        t.name,
                        t.description
                   )
                   FROM Team t
                   WHERE t.id = :id
            """)
    Optional<TeamInfo> findTeamById(@Param("id") Long id);

    @Query(
            """
             SELECT new com.taskmanagement.task_management_system.Model.dto.team.TeamInfo(
                        t.name,
                        t.description
                   )
                 FROM Team t
                   JOIN t.users u
                   WHERE u.id = :id
            """
    )
    List<TeamInfo> findAllTeamsByUserId(Long  id);

    @Query(
            """
             SELECT new com.taskmanagement.task_management_system.Model.dto.team.TeamInfo(
                        t.name,
                        t.description
                   )
                 FROM Team t
            
                   WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """
    )
    List<TeamInfo> findTeamInfoByName(@Param("name") String name);

    @Query(
            """
             SELECT new com.taskmanagement.task_management_system.Model.dto.team.TeamMembersCountResponse(
                  COUNT(u.id)
                   )
                 FROM Team t
                 JOIN t.users u
            WHERE t.id = :id
            """
    )
    TeamMembersCountResponse count(@Param("id") Long id);

    @Query(
            """
            SELECT u.id
            FROM Users u
            WHERE u.id NOT IN (
                SELECT us.id
                FROM Team t
                JOIN t.users us
                WHERE t.id = :teamId
            )
            """
    )
    List<Long> findAvailableUsers(@Param("teamId") Long teamId);

    Boolean existsByUsersIdAndId(Long userId, Long id);

    Page<Team> findAll(Pageable pageable);

}

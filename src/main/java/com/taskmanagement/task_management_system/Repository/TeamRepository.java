package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Model.entity.Team;
import lombok.NonNull;
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
            """)
    TeamInfo findTeamById(Long id);

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


}

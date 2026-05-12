package com.taskmanagement.task_management_system.Repository;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Model.entity.Team;
import lombok.NonNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}

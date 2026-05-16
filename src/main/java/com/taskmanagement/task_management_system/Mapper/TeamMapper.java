package com.taskmanagement.task_management_system.Mapper;


import com.taskmanagement.task_management_system.Model.dto.team.TeamInfo;
import com.taskmanagement.task_management_system.Model.dto.team.TeamRequest;
import com.taskmanagement.task_management_system.Model.dto.team.TeamWithMembers;
import com.taskmanagement.task_management_system.Model.dto.team.UpdateTeamRequest;
import com.taskmanagement.task_management_system.Model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toEntity(TeamRequest dto);
    TeamWithMembers toTeamWithMembers (Team team);
    TeamInfo toDto(Team entity);
    List<TeamInfo> toDtos(List<Team> requests);
    TeamWithMembers membersDto(Team entity);
    void updateTeamFromDto(UpdateTeamRequest dto, @MappingTarget Team entity);

}

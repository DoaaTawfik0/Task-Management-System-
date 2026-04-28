package com.taskmanagement.task_management_system.Mapper;


import com.taskmanagement.task_management_system.Model.dto.TeamInfo;
import com.taskmanagement.task_management_system.Model.dto.TeamRequest;
import com.taskmanagement.task_management_system.Model.dto.UpdateTeamRequest;
import com.taskmanagement.task_management_system.Model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    Team toEntity(TeamRequest dto);
    TeamInfo toDto(Team entity);
    List<TeamInfo> toDtos(List<Team> requests);
    void updateTeamFromDto(UpdateTeamRequest dto, @MappingTarget Team entity);

}

package com.taskmanagement.task_management_system.Mapper;

import com.taskmanagement.task_management_system.Model.dto.TeamResponseDto;
import com.taskmanagement.task_management_system.Model.entity.Team;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamMapper implements Mapper<Team , TeamResponseDto> {
    private final ModelMapper mapper;
    @Override
    public TeamResponseDto mapTo(Team team) {
        return mapper.map(team , TeamResponseDto.class);
    }

    @Override
    public Team mapFrom(TeamResponseDto teamResponseDto) {
        return mapper.map(teamResponseDto , Team.class);
    }
}

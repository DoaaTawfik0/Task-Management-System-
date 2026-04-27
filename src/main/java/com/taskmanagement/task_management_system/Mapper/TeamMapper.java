package com.taskmanagement.task_management_system.Mapper;

import com.taskmanagement.task_management_system.Model.dto.TeamResponseDto;
import com.taskmanagement.task_management_system.Model.dto.UpdateTeamRequestDto;
import com.taskmanagement.task_management_system.Model.entity.Team;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<TeamResponseDto> mapToList(List<Team> a) {
        return a.stream().map(this::mapTo).collect(Collectors.toList());

    }

    public void updateTeam(UpdateTeamRequestDto requestDto , Team team) {
            if(requestDto != null) {
                if(requestDto.getName() != null) team.setName(requestDto.getName());
                if(requestDto.getDescription() != null) team.setDescription(requestDto.getDescription());

            }
    }
}

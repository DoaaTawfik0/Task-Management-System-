package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TeamMapper;
import com.taskmanagement.task_management_system.Model.dto.TeamResponseDto;
import com.taskmanagement.task_management_system.Model.dto.UpdateTeamRequestDto;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService extends BaseService<Team , Long> {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    protected BaseRepository<Team, Long> getRepository() {
        return teamRepository;
    }

    public List<TeamResponseDto> findAllTeams() {
        return teamMapper.mapToList(super.findAll());
    }

    public TeamResponseDto findTeamById(Long id , String name) {
        return teamMapper.mapTo(super.findById(id , name));
    }

    public TeamResponseDto createTeam(TeamResponseDto requestDto) {
        Team team = teamMapper.mapFrom(requestDto);
        Team savedTeam = super.save(team);
        return teamMapper.mapTo(savedTeam);
    }

    public TeamResponseDto updateTeam(Long id , UpdateTeamRequestDto requestDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        teamMapper.updateTeam(requestDto , team);

        Team UpdatedTeam = super.save(team);
        return teamMapper.mapTo(UpdatedTeam);
    }

    @Override
    public void delete(Long id, String name) {
        super.delete(id, name);
    }
}

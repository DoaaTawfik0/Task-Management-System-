package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TeamMapper;
import com.taskmanagement.task_management_system.Model.dto.TeamInfo;
import com.taskmanagement.task_management_system.Model.dto.TeamRequest;
import com.taskmanagement.task_management_system.Model.dto.UpdateTeamRequest;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService extends BaseService<Team, Long> {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    protected BaseRepository<Team, Long> getRepository() {
        return teamRepository;
    }

    public List<TeamInfo> findAllTeams() {
        return teamMapper.toDtos(super.findAll());
    }

    public TeamInfo findTeamById(Long id) {
        return teamMapper.toDto(super.findById(id , "Team"));
    }

    public TeamInfo saveTeam(TeamRequest request) {
        Team team = teamMapper.toEntity(request);
        return teamMapper.toDto(super.save(team));
    }

    public TeamInfo updateTeam(Long id , UpdateTeamRequest dto) {
        Team team = findById(id, "Team");
        teamMapper.updateTeamFromDto(dto , team);
        return teamMapper.toDto(super.save(team));

    }
    @Override
    public void delete(Long id, String name) {
        super.delete(id , name);
    }

}

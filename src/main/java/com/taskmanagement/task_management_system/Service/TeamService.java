package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TeamMapper;
import com.taskmanagement.task_management_system.Model.dto.TeamInfo;
import com.taskmanagement.task_management_system.Model.dto.TeamRequest;
import com.taskmanagement.task_management_system.Model.dto.TeamWithMembers;
import com.taskmanagement.task_management_system.Model.dto.UpdateTeamRequest;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamService extends BaseService<Team, Long> {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
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

    public TeamWithMembers getMembers(Long id) {
        Team team = findById(id , "Team");
        return teamMapper.membersDto(team);
    }

    public TeamWithMembers addMember(Long id , Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id: "+ userId));
        Team team = findById(id,"Team");
        team.getUsers().add(user);
        save(team);
        return teamMapper.membersDto(team);
    }

    public void removeMemberFromTeam(Long teamId, Long userId) {

        Team team = findById(teamId, "Team");

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        team.getUsers().remove(user);

        save(team);
    }

    @Override
    public void delete(Long id, String name) {
        super.delete(id , name);
    }

//    DELETE /teams/{id}/members/{userId}


}

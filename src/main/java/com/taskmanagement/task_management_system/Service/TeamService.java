package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TeamMapper;
import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TeamService extends BaseService<Team, Long> {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;
    private final UserService userService;

    @Override
    protected BaseRepository<Team, Long> getRepository() {
        return teamRepository;
    }

    public List<TeamInfo> findAllTeams() {
        return teamRepository.findAllTeams();
    }

    public TeamInfo findTeamById(Long id) {
        return teamRepository.findTeamById(id);
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

    public List<TeamInfo> getTeamsOfUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException( "User not found with id: " + userId)
        );

        return teamRepository.findAllTeamsByUserId(user.getId());
    }

    @Transactional
    public void addMembers(Long id, List<Long> userIds) {

        Team team = findById(id, "Team");

        List<Users> users = userRepository.findAllById(userIds);

        if(users.size() != userIds.size()) {
            throw new ResourceNotFoundException("Some users were not found");
        }

        for (Users user : users) {
            user.addTeam(team);
        }
    }
    @Transactional
    public void addMember(Long id, Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Team team = findById(id,"Team");
        user.addTeam(team);
        super.save(team);
    }

    public void removeMemberFromTeam(Long teamId, Long userId) {

        Team team = findById(teamId, "Team");

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        team.getUsers().remove(user);
        user.getTeams().remove(team);

        save(team);
    }

    @Override
    public void delete(Long id, String name) {
        super.delete(id , name);
    }


}

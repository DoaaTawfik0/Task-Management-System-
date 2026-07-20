package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.entity.PendingJoiningTeam;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.PendingTeamRepository;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PendingTeamService {
    private final PendingTeamRepository repository;
    private final TeamRepository teamRepository;

    public void createPending(Long userId , Long teamId ) {
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + teamId)

        );
        PendingJoiningTeam pending =
                PendingJoiningTeam
                        .builder()
                        .teamId(team.getId())
                        .userId(userId)
                        .build();

        boolean isRequested = repository.existsByUserIdAndTeamId(userId , teamId);
        boolean isMember = teamRepository.existsByUsersIdAndId(userId , teamId);

        if(isMember) {
            throw new ResourceAlreadyExistException("your are already a member in this team");
        }
        if(isRequested) {
            throw new ResourceAlreadyExistException("your request already sent please wait until your request be approved");
        }
        repository.save(pending);

    }

    public void delete(PendingJoiningTeam pending) {
        repository.delete(pending);
    }


}

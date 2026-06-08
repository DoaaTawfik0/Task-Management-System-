package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Model.entity.PendingJoiningTeam;
import com.taskmanagement.task_management_system.Repository.PendingTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PendingTeamService {
    private final PendingTeamRepository repository;

    public void createPending(Long userId , Long teamId ) {
        PendingJoiningTeam pending =
                PendingJoiningTeam
                        .builder()
                        .teamId(teamId)
                        .userId(userId)
                        .build();

        boolean isRequested = repository.existsByUserIdAndTeamId(userId , teamId);

        if(isRequested) {
            throw new ResourceAlreadyExistException("your request already sent please wait until your request be approved");
        }

        repository.save(pending);

    }

    public void delete(PendingJoiningTeam pending) {
        repository.delete(pending);
    }


}

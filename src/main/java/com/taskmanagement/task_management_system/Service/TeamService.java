package com.taskmanagement.task_management_system.Service;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceAlreadyExistException;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.TeamMapper;
import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Model.entity.PendingJoiningTeam;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.PendingTeamRepository;
import com.taskmanagement.task_management_system.Repository.TeamRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
    private final PendingTeamService pendingTeamService;
    private final PendingTeamRepository pendingTeamRepository;

    @Override
    protected BaseRepository<Team, Long> getRepository() {
        return teamRepository;
    }

    public TeamInfo findTeamById(Long id) {
        return teamRepository.findTeamById(id).orElseThrow(
                () ->
                        new ResourceNotFoundException(
                                "Team not found with id: " + id
                        )
        );
    }

    public TeamInfo createTeam(TeamRequest request , Long userId) {
        Team team = teamMapper.toEntity(request);
        Users user = userService.getUserEntity(userId);
        team.addUser(user);
        return teamMapper.toDto(super.save(team));
    }

    public TeamInfo updateTeam(Long id , UpdateTeamRequest dto , String createdBy) {
        Team team = teamRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: "+ id)
        );
        verifyOwnerOrThrow(team , createdBy);
        teamMapper.updateTeam(dto , team);
        Team updatedTeam = teamRepository.save(team);
        return teamMapper.toDto(updatedTeam);

    }

    public TeamWithMembers getMembers(Long id , String createdBy) {
        Team team = teamRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + id)
        );
        verifyOwnerOrThrow(team , createdBy);
        return teamMapper.membersDto(team);
    }

    public List<TeamInfo> getTeamsOfUser(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException( "User not found with id: " + userId)
        );

        return teamRepository.findAllTeamsByUserId(user.getId());
    }

    @Transactional
    public void addMembers(Long id, List<Long> userIds , String createdBy) {

        Team team = teamRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + id)
        );
        verifyOwnerOrThrow(team , createdBy);

        List<Users> users = userRepository.findAllById(userIds);
        boolean isMember ;

        if(users.size() != userIds.size()) {
            throw new ResourceNotFoundException("Some users were not found");
        }

        for (Users user : users) {
          isMember= teamRepository.existsByUsersIdAndId(user.getId() , id);
            if(isMember) {
                throw new ResourceAlreadyExistException("user with id " + user.getId() + " is already a member of team " + id);
            }

            user.addTeam(team);
        }
    }
    @Transactional
    public void addMember(Long id, Long userId , String createdBy) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Team team = teamRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + id)
        );
        verifyOwnerOrThrow(team , createdBy);

        boolean isMember = teamRepository.existsByUsersIdAndId(userId , id);
        if(isMember) {
            throw new ResourceAlreadyExistException("user with id " + userId + " is already a member of team " + id);
        }
        user.addTeam(team);
        super.save(team);
    }

    public void removeMemberFromTeam(Long teamId, Long userId , String createdBy) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + teamId)
        );

        verifyOwnerOrThrow(team , createdBy);

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));


        boolean isMember = teamRepository.existsByUsersIdAndId(user.getId() , team.getId());

        if(!isMember) {
            throw new ResourceNotFoundException(
                    "User is not a member of this team"
            );
        }

        team.getUsers().remove(user);
        user.getTeams().remove(team);

        save(team);
    }

    public TeamMembersCountResponse countTeamMembers(Long teamId , Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
                    ()-> new ResourceNotFoundException("user not found with id: " + userId)
        );
        Team team = teamRepository.findById(teamId).orElseThrow(
                  ()-> new ResourceNotFoundException("team not found with id: " + teamId)
        );

        boolean isMember = teamRepository.existsByUsersIdAndId(user.getId() , team.getId());
        if(!isMember) {
            throw  new ResourceNotFoundException("join to the team to see the details of this team");
        }

        return teamRepository.count(team.getId());
    }

    public List<TeamInfo> search(String teamName) {
      return teamRepository.findTeamInfoByName(teamName);

    }
    public TeamAvailableUsers getAvailableUsers(Long teamId , Long userId) {

        String createdBy = userService.getUserEntity(userId).getUsername();

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "team not found with id: "+ teamId
                )
        );

        verifyOwnerOrThrow(team , createdBy);

        List<Long> userIds = teamRepository.findAvailableUsers(teamId);

        return TeamAvailableUsers.builder()
                .teamId(team.getId())
                .userIds(userIds)
                .build();
    }
    public boolean isExists(Long userId , Long teamId , String createdBy) {
        if(!userRepository.existsById(userId))  {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }
        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException( "Team not found with id: " + teamId)
        );
        verifyOwnerOrThrow(team , createdBy);

        return teamRepository.existsByUsersIdAndId(userId , team.getId());

    }

    public Page<TeamInfo> getAll(Pageable pageable , UserRole role , String createdBy) {
        if(role == UserRole.ADMIN) {
            return teamRepository.findAllTeams(pageable);
        }else {
            return teamRepository.findByCreatedBy(createdBy , pageable);
        }

    }

    public TeamWithMembers replaceMembers(Long teamId, AddUsersToTeamRequest request , String createdBy) {

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException("team not found with id: " + teamId)
        );

        verifyOwnerOrThrow(team , createdBy);

        List<Users> users = userRepository.findAllById(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new ResourceNotFoundException("Some users not found");
        }


        List<Users> currentUsers = new ArrayList<>(team.getUsers());
        for (Users user : currentUsers) {
            team.removeUser(user);
        }

        for (Users user : users) {
            team.addUser(user);
        }

        Team savedTeam = teamRepository.save(team);

        return teamMapper.toTeamWithMembers(savedTeam);
    }

    public void leaveTeam(Long teamId, Long userId) {

        Team team = super.findById(teamId, "Team");

        Users user = userService.getUserEntity(userId);


        boolean isMember = team.getUsers().contains(user);

        if (!isMember) {
            throw new ResourceNotFoundException(
                    "User is not a member of this team"
            );
        }

        team.removeUser(user);

        teamRepository.save(team);
    }

    @Transactional
    public void approveRequest(Long pendingId , String createdBy) {
        PendingJoiningTeam pending = pendingTeamRepository.findById(pendingId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("pending request not found")
                );
        Users user = userRepository.findById(pending.getUserId())
                .orElseThrow(
                        ()->new ResourceNotFoundException("user with id: " + pending.getUserId() + " not found")
                );
        Team team = teamRepository.findById(pending.getTeamId()).orElseThrow(
                ()->new ResourceNotFoundException("team with id: " + pending.getTeamId() + " not found")

        );
        verifyOwnerOrThrow(team , createdBy);
        team.addUser(user);
        pendingTeamRepository.delete(pending);
    }

    @Override
    public void delete(Long id, String name) {
        Team team = findById(id , name);
        Set<Users> users = new HashSet<>(team.getUsers());
       for(Users user: users){
           team.removeUser(user);
       }
        teamRepository.delete(team);
    }

    public Long countTeams(Long userId){
        return teamRepository.countByUsersId(userId);
    }


    ///  validations

    ///  create teams -> admin - manager
    public void verifyOwnerOrThrow(Team team , String username) {
        if(!team.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("team not found with id: " + team.getId());
        }

    }

    ///  get role
     public void verifyCurrentCanCreate(CustomUserDetails currentUser) {
         Users user = currentUser.user();
         UserRole role = user.getRole();
         if(role == UserRole.USER) {
             throw new AccessDeniedException("you don't have permissions to perform this action.");
         }

     }

}

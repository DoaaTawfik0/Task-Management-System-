package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Service.PendingTeamService;
import com.taskmanagement.task_management_system.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService service;
    private final PendingTeamService pendingTeamService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{id}")
    public ResponseEntity<TeamInfo> get(@PathVariable Long id) {
        return  ResponseEntity.ok(service.findTeamById(id));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<Page<TeamInfo>> getAll
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page , size);
        return ResponseEntity.ok(service.getAll(pageable));
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> getTeamMembers(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMembers(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<List<TeamInfo>> getMyTeams(@AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.getTeamsOfUser(current.user().getId()));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/{teamId}/members/count")
    public ResponseEntity<TeamMembersCountResponse> count(@PathVariable Long teamId){
        return ResponseEntity.ok(service.countTeamMembers(teamId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{teamId}/members/{userId}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long userId , @PathVariable Long teamId) {

        return ResponseEntity.ok(service.isExists(userId , teamId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{teamId}/available-users")
    public ResponseEntity<TeamAvailableUsers> getAvailableUsers(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getAvailableUsers(teamId));
    }
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('MANAGER')")
    @GetMapping("/search")
    public ResponseEntity<List<TeamInfo>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping
    public ResponseEntity<TeamInfo> add(@RequestBody TeamRequest request) {
        return ResponseEntity.ok(service.saveTeam(request));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("{teamId}/join")
    ResponseEntity<String> join(
            @AuthenticationPrincipal CustomUserDetails current
            , @PathVariable("teamId") Long teamId) {
           pendingTeamService.createPending(current.user().getId() , teamId);
           return ResponseEntity.ok("request sent successfully please wait until your request be approved");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<String> add(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        service.addMember(teamId , userId);
        return ResponseEntity.ok("User with id:" + userId + " is Added successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{requestId}/approve-request")
    public ResponseEntity<Void> approve(@PathVariable("requestId") Long requestId) {
        service.approveRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{teamId}/members/bulk")
    public ResponseEntity<String> addMultiple(
            @PathVariable Long teamId
            ,@RequestBody List<Long>  request){
        service.addMembers(teamId , request);
        return ResponseEntity.ok("Users is Added successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TeamInfo> update(
            @PathVariable Long id,
            @RequestBody UpdateTeamRequest dto) {
        return ResponseEntity.ok(service.updateTeam(id , dto));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> replaceMembers(@PathVariable Long id, @RequestBody AddUsersToTeamRequest request) {
        return ResponseEntity.ok(service.replaceMembers(id, request));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        service.removeMemberFromTeam(teamId, userId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("{teamId}/leave/{userId}")
    public ResponseEntity<Void> leaveTeam(@PathVariable Long teamId, @PathVariable Long userId) {

        service.leaveTeam(teamId, userId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id , "Team");
        return ResponseEntity.noContent().build();
    }
}

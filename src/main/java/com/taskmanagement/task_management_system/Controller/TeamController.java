package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Mapper.TeamMapper;
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
    private final TeamMapper mapper;

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TeamInfo> get(@PathVariable Long id) {
        return  ResponseEntity.ok(service.findTeamById(id));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TeamInfo>> getAll
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size,
             @AuthenticationPrincipal CustomUserDetails current) {
        Pageable pageable = PageRequest.of(page , size);
        return ResponseEntity.ok(service.getAll(pageable , current.user().getRole() , current.getUsername()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> getTeamMembers(@PathVariable Long id , @AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.getMembers(id , current.getUsername()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<TeamInfo>> getAllTeamsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTeamsOfUser(id));
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<List<TeamInfo>> getMyTeams(@AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.getTeamsOfUser(current.user().getId()));
    }

    @PreAuthorize("hasAnyRole('MANAGER','USER')")
    @GetMapping("/{teamId}/members/count")
    public ResponseEntity<TeamMembersCountResponse> count(@PathVariable Long teamId , @AuthenticationPrincipal CustomUserDetails current){
        return ResponseEntity.ok(service.countTeamMembers(teamId , current.user().getId()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{teamId}/members/{userId}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long userId , @PathVariable Long teamId , @AuthenticationPrincipal CustomUserDetails current) {

        return ResponseEntity.ok(service.isExists(userId , teamId , current.getUsername()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/{teamId}/available-users")
    public ResponseEntity<TeamAvailableUsers> getAvailableUsers(@PathVariable Long teamId , @AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.getAvailableUsers(teamId , current.user().getId()));
    }

    @PreAuthorize("hasAnyRole('USER','MANAGER','ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<TeamInfo>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @PostMapping
    public ResponseEntity<TeamInfo> create(@RequestBody TeamRequest request , @AuthenticationPrincipal CustomUserDetails current) {
        service.verifyCurrentCanCreate(current);
        return ResponseEntity.ok(service.createTeam(request , current.user().getId()));
    }

    @PreAuthorize("hasAnyRole('MANAGER','USER')")
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
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails current) {
        service.addMember(teamId , userId , current.getUsername());
        return ResponseEntity.ok("User with id:" + userId + " is Added successfully...");
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{requestId}/approve-request")
    public ResponseEntity<Void> approve(@PathVariable("requestId") Long requestId,
                                        @AuthenticationPrincipal CustomUserDetails current                             ) {
        service.approveRequest(requestId , current.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/{teamId}/members/bulk")
    public ResponseEntity<String> addMultiple(
            @PathVariable Long teamId,
            @AuthenticationPrincipal CustomUserDetails current,
            @RequestBody List<Long>  request){
        service.addMembers(teamId , request , current.getUsername());
        return ResponseEntity.ok("Users is Added successfully...");
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<TeamInfo> update(
            @PathVariable Long id,
            @RequestBody UpdateTeamRequest dto,
            @AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.updateTeam(id , dto , current.getUsername()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> replaceMembers(@PathVariable Long id,
                                                          @RequestBody AddUsersToTeamRequest request,
                                                          @AuthenticationPrincipal CustomUserDetails current) {
        return ResponseEntity.ok(service.replaceMembers(id, request , current.getUsername()));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails current){

        service.removeMemberFromTeam(teamId, userId , current.getUsername());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER' , 'USER')")
    @DeleteMapping("{teamId}/members/me")
    public ResponseEntity<Void> leaveTeam(@PathVariable Long teamId,@AuthenticationPrincipal CustomUserDetails current) {

        service.leaveTeam(teamId, current.user().getId());

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id , "Team");
        return ResponseEntity.noContent().build();
    }
}

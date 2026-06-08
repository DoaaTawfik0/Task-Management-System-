package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService service;

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfo> get(@PathVariable Long id) {
        return  ResponseEntity.ok(service.findTeamById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TeamInfo>> getAll
            (@RequestParam(defaultValue = "0") int page,
             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page , size);
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> getTeamMembers(@PathVariable Long id) {
        return ResponseEntity.ok(service.getMembers(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TeamInfo>> getAllTeamsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTeamsOfUser(id));
    }
    @GetMapping("/{teamId}/members/count")
    public ResponseEntity<TeamMembersCountResponse> count(@PathVariable Long teamId){
        return ResponseEntity.ok(service.countTeamMembers(teamId));
    }
    @GetMapping("/{teamId}/members/{userId}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long userId , @PathVariable Long teamId) {

        return ResponseEntity.ok(service.isExists(userId , teamId));
    }
    @GetMapping("/{teamId}/available-users")
    public ResponseEntity<TeamAvailableUsers> getAvailableUsers(@PathVariable Long teamId) {
        return ResponseEntity.ok(service.getAvailableUsers(teamId));
    }
    @GetMapping("/search")
    public ResponseEntity<List<TeamInfo>> search(@RequestParam String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PostMapping
    public ResponseEntity<TeamInfo> add(@RequestBody TeamRequest request) {
        return ResponseEntity.ok(service.saveTeam(request));
    }

    @PostMapping("{teamId}/join")
    ResponseEntity<String> join(
            @AuthenticationPrincipal CustomUserDetails current
            , @PathVariable("teamId") Long teamId) {
        service.addMember(teamId , current.user().getId());
        return ResponseEntity.ok("user joined successfully...");
    }

    @PostMapping("/{teamId}/members/{userId}")
    public ResponseEntity<String> add(
            @PathVariable Long teamId,
            @PathVariable Long userId) {
        service.addMember(teamId , userId);
        return ResponseEntity.ok("User with id:" + userId + " is Added successfully...");
    }

    @PostMapping("/{teamId}/members/bulk")
    public ResponseEntity<String> addMultiple(
            @PathVariable Long teamId
            ,@RequestBody List<Long>  request){
        service.addMembers(teamId , request);
        return ResponseEntity.ok("Users is Added successfully...");
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamInfo> update(
            @PathVariable Long id,
            @RequestBody UpdateTeamRequest dto) {
        return ResponseEntity.ok(service.updateTeam(id , dto));
    }
    @PutMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> replaceMembers(@PathVariable Long id, @RequestBody AddUsersToTeamRequest request) {
        return ResponseEntity.ok(service.replaceMembers(id, request));
    }

    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity removeMember(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        service.removeMemberFromTeam(teamId, userId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{teamId}/leave/{userId}")
    public ResponseEntity<Void> leaveTeam(@PathVariable Long teamId, @PathVariable Long userId) {

        service.leaveTeam(teamId, userId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id , "Team");
        return ResponseEntity.noContent().build();
    }
}

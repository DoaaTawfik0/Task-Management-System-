package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Model.entity.Team;
import com.taskmanagement.task_management_system.Service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService service;

    @GetMapping
    public ResponseEntity<List<TeamInfo>> getAllTeams() {
        return ResponseEntity.ok(service.findAllTeams());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamInfo> get(@PathVariable Long id) {
        return  ResponseEntity.ok(service.findTeamById(id));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<TeamWithMembers> getByMembers(@PathVariable Long id) {
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
    @GetMapping("/")
    public ResponseEntity<Page<Team>> getAll(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(service.getAll(page, size));
    }
    @GetMapping("/search/{name}")
    public ResponseEntity<List<TeamInfo>> search(@PathVariable String name) {
        return ResponseEntity.ok(service.search(name));
    }

    @PostMapping
    public ResponseEntity<TeamInfo> add(@RequestBody TeamRequest request) {
        return ResponseEntity.ok(service.saveTeam(request));
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<String> add(
            @PathVariable Long id,
            @PathVariable Long userId) {
        service.addMember(id , userId);
        return ResponseEntity.ok("User with id:" + userId + " is Added successfully...");
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leaveTeam(@PathVariable Long id, @PathVariable Long userId) {

        service.leaveTeam(id, userId);

        return ResponseEntity.noContent().build();
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

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id , "Team");
        return ResponseEntity.noContent().build();
    }
}

package com.taskmanagement.task_management_system.Controller;

import com.taskmanagement.task_management_system.Model.dto.team.*;
import com.taskmanagement.task_management_system.Service.TeamService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/teams/user/{id}")
    public ResponseEntity<List<TeamInfo>> getAllTeamsByUser(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTeamsOfUser(id));
    }

    @PostMapping
    public ResponseEntity<TeamInfo> add(@RequestBody TeamRequest request) {
        return ResponseEntity.ok(service.saveTeam(request));
    }

    @PostMapping("/{id}/members/{userId}")
    public ResponseEntity<TeamWithMembers> add(
            @PathVariable Long id,
            @PathVariable Long userId) {
        return ResponseEntity.ok(service.addMember(id , userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamInfo> update(
            @PathVariable Long id,
            @RequestBody UpdateTeamRequest dto) {
        return ResponseEntity.ok(service.updateTeam(id , dto));
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

package com.taskmanagement.task_management_system.Model.dto.team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUsersToTeamRequest {

    private List<Long> userIds;
}
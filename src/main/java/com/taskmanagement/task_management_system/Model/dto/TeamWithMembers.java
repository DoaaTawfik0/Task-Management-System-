package com.taskmanagement.task_management_system.Model.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamWithMembers {
    private Long id;
    private String name;
    private List<UserInfo> users;

}

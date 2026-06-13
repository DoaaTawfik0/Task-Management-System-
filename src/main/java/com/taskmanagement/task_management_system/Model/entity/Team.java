package com.taskmanagement.task_management_system.Model.entity;

import com.taskmanagement.task_management_system.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Team extends BaseEntity<Long> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @ManyToMany(mappedBy = "teams")
    private Set<Users> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team")
    private List<Task> tasks = new ArrayList<>();

    public void addUser(Users user) {

        if (!users.contains(user)) {
            users.add(user);
        }

        if (!user.getTeams().contains(this)) {
            user.getTeams().add(this);
        }
    }
    public void removeUser(Users user) {

        users.remove(user);
        user.getTeams().remove(this);
    }


}

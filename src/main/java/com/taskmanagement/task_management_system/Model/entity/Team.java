package com.taskmanagement.task_management_system.Model.entity;

import com.taskmanagement.task_management_system.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "teams")
    private List<Users> users = new ArrayList<>();

    @OneToMany(mappedBy = "team")
    private List<Task> tasks = new ArrayList<>();

}

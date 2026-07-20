package com.taskmanagement.task_management_system.Model.entity;

import com.taskmanagement.task_management_system.Base.BaseEntity;
import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
public class Task extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    // Relationships

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "task_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"task_id", "user_id"})
    )
    private Set<Users> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "task",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public void addComment(Comment comment) {
        if (comment != null) {
            comments.add(comment);
            comment.setTask(this);
        }
    }

    public void addUser(Users user) {
        if (user != null) {
            this.users.add(user);
            user.getTasks().add(this);
        }
    }

    public void removeUser(Users user) {
        if (user != null) {
            this.users.remove(user);
            user.getTasks().remove(this);
        }
    }


}

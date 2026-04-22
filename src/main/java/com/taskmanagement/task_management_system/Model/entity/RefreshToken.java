package com.taskmanagement.task_management_system.Model.entity;


import com.taskmanagement.task_management_system.Base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseEntity<Long> {
    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne
    private Users user;

    private LocalDateTime expiryDate;

    private boolean revoked;
}
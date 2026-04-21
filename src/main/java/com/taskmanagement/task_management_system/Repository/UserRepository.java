package com.taskmanagement.task_management_system.Repository;


import com.taskmanagement.task_management_system.Model.entity.Users;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<@NonNull Users, @NonNull Long> {
    Optional<Users> findByEmail(String email);

    Boolean existsByEmail(String email);
}

package com.taskmanagement.task_management_system.Repository;


import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Enum.AuthProvider;
import com.taskmanagement.task_management_system.Model.dto.user.UserInfo;
import com.taskmanagement.task_management_system.Model.entity.Users;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends BaseRepository<@NonNull Users, @NonNull Long> {
    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("""
                SELECT new com.taskmanagement.task_management_system.Model.dto.user.UserInfo(
                    u.username,
                    CONCAT(u.firstName, ' ', u.lastName),
                    u.email,
                    u.role
                )
                FROM Users u
            """)
    Page<UserInfo> findAllUsers(Pageable pageable);

    @Query("""
            SELECT new com.taskmanagement.task_management_system.Model.dto.user.UserInfo(
                u.username,
                CONCAT(u.firstName, ' ', u.lastName),
                u.email,
                u.role
            )
            FROM Users u
            WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(CONCAT(u.firstName, ' ', u.lastName))
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<UserInfo> searchUsersByKeyword(String keyword);

    Optional<Users> findByProviderAndProviderId(
            AuthProvider provider,
            String providerId
    );

}

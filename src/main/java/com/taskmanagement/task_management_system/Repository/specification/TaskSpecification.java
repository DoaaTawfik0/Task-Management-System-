package com.taskmanagement.task_management_system.Repository.specification;

import com.taskmanagement.task_management_system.Enum.Priority;
import com.taskmanagement.task_management_system.Enum.Status;
import com.taskmanagement.task_management_system.Model.entity.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> hasStatus(Status status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);

    }

    public static Specification<Task> hasPriority(Priority priority) {
        return (root, query, cb) ->
                cb.equal(root.get("priority"), priority);
    }

    public static Specification<Task> assignedToUser(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.join("users").get("id"), userId);
    }

}

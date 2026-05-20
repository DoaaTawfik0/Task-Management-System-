package com.taskmanagement.task_management_system.Repository.task;

import com.taskmanagement.task_management_system.Model.dto.task.TaskInfo;
import com.taskmanagement.task_management_system.Model.entity.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryCustom {

    private final EntityManager entityManager;

    @Override
    public List<TaskInfo> findAllTasks(Specification<Task> spec) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskInfo> query = cb.createQuery(TaskInfo.class);

        Root<Task> root = query.from(Task.class);

        query.select(cb.construct(
                TaskInfo.class,
                root.get("title"),
                root.get("description"),
                root.get("priority"),
                root.get("status"),
                root.get("dueDate")
        ));

        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);

            if (predicate != null) {
                query.where(predicate);
            }
        }

        return entityManager.createQuery(query).getResultList();
    }
}

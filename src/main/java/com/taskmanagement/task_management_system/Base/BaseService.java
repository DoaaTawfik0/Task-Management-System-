package com.taskmanagement.task_management_system.Base;

import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getNotificationRepository();

    public Page<T> findAllInPages(Pageable pageable) {
        return getNotificationRepository().findAll(pageable);
    }

    public List<T> findAll() {
        return getNotificationRepository().findAll();
    }


    public T findById(ID id, String name) {
        return getNotificationRepository().findById(id)

                .orElseThrow(() -> new ResourceNotFoundException("Resource(" + name + ") with id: " + id + " does not exist"));
    }

    public T save(T entity) {
        return getNotificationRepository().save(entity);
    }

    public void delete(ID id, String name) {
        T savedEntity = findById(id, name);
        getNotificationRepository().delete(savedEntity);
    }

}
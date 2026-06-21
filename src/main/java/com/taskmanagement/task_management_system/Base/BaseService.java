package com.taskmanagement.task_management_system.Base;

import com.taskmanagement.task_management_system.Enum.UserRole;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Model.CustomUserDetails;
import com.taskmanagement.task_management_system.Model.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseService<T extends BaseEntity<?>, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    public Page<T> findAllInPages(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }


    public T findById(ID id, String name) {
        return getRepository().findById(id)

                .orElseThrow(() -> new ResourceNotFoundException("Resource(" + name + ") with id: " + id + " does not exist"));
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id, String name) {
        T savedEntity = findById(id, name);
        getRepository().delete(savedEntity);
    }

    public Boolean checkExistence(ID id, String name) {
        boolean existed = getRepository().existsById(id);
        if (!existed) {
            throw new ResourceNotFoundException("Resource " + name + " with id " + id + " does not exist");
        }
        return true;
    }


    /**
     * Validates ownership of a Resource before allowing modifications.
     */
    public void verifyOwnerOrThrow(T entity, String username) {
        if (!entity.getCreatedBy().equals(username)) {
            throw new AccessDeniedException("Access denied: you aren't the owner of this Resource(" + entity.getClass().getSimpleName() + ")");
        }
    }

    public void verifyOwnerOrAdmin(T entity, CustomUserDetails currentUser) {
        Users user = currentUser.user();

        boolean owner = isOwner(entity, user.getUsername());
        boolean admin = hasRole(user, UserRole.ADMIN);

        if (!owner && !admin) {
            throw new AccessDeniedException(
                    "You are not allowed to perform this operation on this resource ("
                            + entity.getClass().getSimpleName() + ")"
            );
        }
    }

    public boolean isOwner(T entity, String username) {
        return entity.getCreatedBy().equals(username);
    }

    public boolean hasRole(Users currentUser, UserRole role) {
        return currentUser.getRole().equals(role);
    }

}
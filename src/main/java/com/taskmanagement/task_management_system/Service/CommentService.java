package com.taskmanagement.task_management_system.Service;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Mapper.CommentMapper;
import com.taskmanagement.task_management_system.Model.dto.CommentInfo;
import com.taskmanagement.task_management_system.Model.dto.comment.CommentRequest;
import com.taskmanagement.task_management_system.Model.entity.Comment;
import com.taskmanagement.task_management_system.Model.entity.Task;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService extends BaseService<Comment, Long> {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final UserService userService;

    @Override
    protected BaseRepository<Comment, Long> getRepository() {
        return repository;
    }

    public CommentInfo addComment(Task task, CommentRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        Users user = userService.findByEmail(email);

        Comment comment = mapper.toEntity(request);

        task.addComment(comment);

        comment.setUser(user);

        Comment saved = repository.save(comment);

        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentInfo> getTaskComments(Task task) {

        return mapper.toDtos(task.getComments());
    }

    public CommentInfo updateComment(Long id, CommentRequest request) {

        Comment comment = super.findById(id, Comment.class.getSimpleName());

        mapper.updateComment(request, comment);

        Comment updated = repository.save(comment);

        return mapper.toDto(updated);
    }

    public void deleteComment(Long id) {

        super.delete(id, Comment.class.getSimpleName());
    }
}
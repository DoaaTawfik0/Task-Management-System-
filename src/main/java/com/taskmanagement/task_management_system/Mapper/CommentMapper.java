package com.taskmanagement.task_management_system.Mapper;

import com.taskmanagement.task_management_system.Model.dto.comment.CommentInfo;
import com.taskmanagement.task_management_system.Model.dto.comment.CommentRequest;
import com.taskmanagement.task_management_system.Model.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentRequest dto);

    CommentInfo toDto(Comment entity);

    List<CommentInfo> toDtos(List<Comment> entities);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(CommentRequest dto,
                       @MappingTarget Comment entity);
}
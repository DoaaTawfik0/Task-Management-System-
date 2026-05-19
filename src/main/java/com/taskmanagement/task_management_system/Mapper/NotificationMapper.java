package com.taskmanagement.task_management_system.Mapper;

import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NotificationRequest request);
    Notification toEntityResponse(NotificationResponse request);
    NotificationRequest toDto(Notification entity);
    NotificationResponse toResponse(Notification entity);
    List<NotificationResponse> toDtos(List<Notification> notifications);
    List<Notification> toResponses(List<NotificationResponse> notifications);
    List<Notification> toEntities(List<NotificationRequest> requests);
    List<Notification> Responses(List<NotificationResponse> requests);



}

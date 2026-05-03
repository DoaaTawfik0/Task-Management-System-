package com.taskmanagement.task_management_system.Service.notification;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Base.NotificationSender;
import com.taskmanagement.task_management_system.Enum.NotificationStatus;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.NotificationMapper;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import com.taskmanagement.task_management_system.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService extends BaseService<Notification , Long> {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final List<NotificationSender> senders;


    @Override
    protected BaseRepository<Notification, Long> getRepository() {
        return repository;
    }

    public List<NotificationResponse> getAll(){
        return mapper.toDtos(repository.findAll());
    }

    public void markAsRead(Long id) {
        Notification notification = repository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("notification not found with id: "+ id)
        );

        notification.setNotificationStatus(NotificationStatus.READ);

        repository.save(notification);
    }

    @Async
    public void sendNotification(NotificationRequest request) {

        NotificationSender sender = senders.stream()
                .filter(s -> s.getType().equals(request.getNotificationType()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No sender found for type: " + request.getNotificationType()
                ));

        Notification notification = mapper.toEntity(request);
        notification.setNotificationStatus(NotificationStatus.UNREAD);

        repository.save(notification);

        try {
            sender.send(request);
        } catch (Exception e) {
            throw e;
        }

        repository.save(notification);
    }

}

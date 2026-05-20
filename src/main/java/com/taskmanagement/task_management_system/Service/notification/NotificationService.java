package com.taskmanagement.task_management_system.Service.notification;

import com.taskmanagement.task_management_system.Base.BaseRepository;
import com.taskmanagement.task_management_system.Base.BaseService;
import com.taskmanagement.task_management_system.Base.NotificationSender;
import com.taskmanagement.task_management_system.Enum.NotificationStatus;
import com.taskmanagement.task_management_system.Exception.Resource.ResourceNotFoundException;
import com.taskmanagement.task_management_system.Mapper.NotificationMapper;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationRequest;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationResponse;
import com.taskmanagement.task_management_system.Model.dto.notification.NotificationUnreadResponse;
import com.taskmanagement.task_management_system.Model.entity.Notification;
import com.taskmanagement.task_management_system.Model.entity.Users;
import com.taskmanagement.task_management_system.Repository.NotificationRepository;
import com.taskmanagement.task_management_system.Repository.UserRepository;
import com.taskmanagement.task_management_system.Utility.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class NotificationService extends BaseService<Notification , Long> {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;
    private final List<NotificationSender> senders;
    private final JwtService jwt;
    private final UserRepository userRepository;


    @Override
    protected BaseRepository<Notification, Long> getRepository() {
        return notificationRepository;
    }

    public List<NotificationResponse> getAll(Long userId){

        return notificationRepository.findAllByUserId(userId);
    }

    public NotificationResponse getById(Long id , Long userId){
        boolean flag = notificationRepository.existsByUserIdAndId(userId , id);
        if(!flag) {
            throw new ResourceNotFoundException("notification not found with user id: "+userId);
        }

        return notificationRepository.findNotificationById(id);
    }

    public List<NotificationResponse> getUnread(Long userId) {
        return notificationRepository.findAllUnreadByUserId(userId);
    }

    public NotificationUnreadResponse countUnread(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public void markAsRead(Long userId , Long id) {

       int updated = notificationRepository.markAsRead(userId , id);
       if(updated == 0) {
           throw new ResourceNotFoundException("notification not found with user id: "+userId);

       }
    }
@Transactional
    public void markAllAsRead(Long userId) {
     int updated = notificationRepository.markAllAsRead(userId);
        if(updated == 0) {
            throw new ResourceNotFoundException("notification not found with user id: "+ userId);

        }
    }

    @Async
    public void sendNotification(NotificationRequest request) {

        NotificationSender sender = senders.stream()
                .filter(s -> s.getType().equals(request.getNotificationType()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No sender found for type: " + request.getNotificationType()
                ));

        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + request.getUserId()
                ));

        Notification notification = mapper.toEntity(request);
        notification.setUser(user);
        notification.setNotificationStatus(NotificationStatus.UNREAD);

        notificationRepository.save(notification);

        try {
            sender.send(request);
        } catch (UnableToSendNotificationException e) {
            throw new UnableToSendNotificationException(e.getMessage());
        }
    }
    public void validateNotificationRequest(NotificationRequest request) {

        if (!userRepository.existsById(request.getUserId())) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + request.getUserId()
            );
        }
    }

    @Transactional
    public void delete(Long id ,Long userId) {

        int deleted = notificationRepository.deleteByIdAndUserId(id,userId);
        if(deleted == 0) {
            throw new ResourceNotFoundException("notification not found with id: "+ id +
                    " and user id: "+ userId);

        }
    }

    @Transactional
    public void deleteAll(Long userId) {

       int deleted = notificationRepository.deleteAllByUserId(userId);
       if(deleted == 0) {
           throw new ResourceNotFoundException("notification not found with user id: "+ userId);

       }

    }
}
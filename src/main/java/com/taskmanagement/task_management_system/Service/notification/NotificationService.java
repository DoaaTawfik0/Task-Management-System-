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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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

    public List<NotificationResponse> getAll(String authHeader){
        String token = jwt.extractToken(authHeader);
        String email = jwt.extractEmail(token);

        Users user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user not found by email: "+ email)
        );

        return notificationRepository.findAllByUserId(user.getId());
    }

    public NotificationRequest getById(Long id , String authHeader){
        String token = jwt.extractToken(authHeader);
        String email = jwt.extractEmail(token);

        Users user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user not found by email: "+ email)
        );
        Notification notification = notificationRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Notification not found with id: " + id)
        );

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this notification");
        }

        return mapper.toDto(notification);
    }

    public List<NotificationResponse> getUnread(String authHeader) {
        String token = jwt.extractToken(authHeader);
        String email = jwt.extractEmail(token);
        Users user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user not found by email: "+ email)
        );
        return notificationRepository.findAllUnreadByUserId(user.getId());
    }

    public NotificationUnreadResponse countUnread(String auth) {
        String token = jwt.extractToken(auth);
        String email = jwt.extractEmail(token);
        Users user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("user not found by email: "+ email)
        );
        return notificationRepository.countUnreadByUserId(user.getId());
    }

    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("notification not found with id: "+ id)
        );

        notification.setNotificationStatus(NotificationStatus.READ);

        notificationRepository.save(notification);
    }

    public void markAllAsRead() {
        List<Notification> notification = notificationRepository.findAll();

        for (Notification notifications:notification) {
            notifications.setNotificationStatus(NotificationStatus.READ);

        }
        notificationRepository.saveAll(notification);
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Notification notification = mapper.toEntity(request);
        notification.setUser(user);
        notification.setNotificationStatus(NotificationStatus.UNREAD);

        notificationRepository.save(notification);

        try {
            sender.send(request);
        } catch (Exception e) {
            throw e;
        }

        notificationRepository.save(notification);
    }

    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("notification not found with id: "+ id)
        );

        super.delete(id , "notification");
    }

    public void deleteAll() {
        notificationRepository.deleteAll();
    }
}
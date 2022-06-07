package com.onit.onit_be.noti;


import com.onit.onit_be.entity.Plan;
import com.onit.onit_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteAllByUser(User user);

    List<Notification> findAllByUser(User user);

    Optional<Notification> findByUserAndPlanAndAndNotificationType(User user, Plan plan, NotificationType notificationType);
}

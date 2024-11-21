package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllById(Long id);

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :id AND n.isRead = false")
    List<Notification> findUnreadListById(@Param("id") Long id);

}

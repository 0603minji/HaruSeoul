package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

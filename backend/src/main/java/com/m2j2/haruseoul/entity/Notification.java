package com.m2j2.haruseoul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" , nullable = false)
    private Long id;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "type", nullable = false)
    private String type;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime regDate;

    @ColumnDefault("false")
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

}

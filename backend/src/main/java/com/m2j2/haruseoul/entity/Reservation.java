package com.m2j2.haruseoul.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 프로그램 ID = 날짜가 정해진 프로그램
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "published_program_id", nullable = false)
    @JsonBackReference
    private PublishedProgram publishedProgram;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonBackReference
    private Member member;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date", nullable = false)
    @CreationTimestamp
    private Instant regDate;

    @Column(name = "delete_date")
    private Instant deleteDate;

    @Column(name = "number_of_guest", nullable = false)
    private Integer numberOfGuest;

    @Column(name = "requirement")
    private String requirement;

    @Column(name = "cancel_method")
    private Integer cancelMethod;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "reservation_status", nullable = false)
    private Integer reservationStatus ;

    @Column(name = "guest_consent")
    private Integer guestConsent;

}
package com.m2j2.haruseoul.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalTime;


@Data
@Entity
@Table(name = "program")
public class Program {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    private Member regMember;

    @Column(name = "title")
    private String title;

    @Column(name = "detail", length = 6000)
    private String detail;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date")
    private Instant regDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "status")
    private String status;

    @Column(name = "price")
    private Integer price;

    @Column(name = "group_size_max")
    private Integer groupSizeMax;

    @Column(name = "group_size_min")
    private Integer groupSizeMin;

    @Column(name = "rating")
    private Float rating;

    @ColumnDefault("current_timestamp()")
    @Column(name = "update_date")
    private Instant updateDate;

    @Column(name = "category1")
    private String category1;

    @Column(name = "category2")
    private String category2;

    @Column(name = "language")
    private String language;

    @Column(name = "start_time")
    private LocalTime startTime;

}
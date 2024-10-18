package com.m2j2.haruseoul.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "program")
public class Program {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "detail", length = 6000)
    private String detail;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date")
    private Instant regDate;

    @Column(name = "end_time")
    private Instant endTime;

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

    @Column(name = "language")
    private String language;

    @Column(name = "start_time")
    private Instant startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    @JsonBackReference
    private Member member;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<Image> images;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "program")
    @JsonManagedReference
    private List<CategoryProgram> categoryPrograms;

}
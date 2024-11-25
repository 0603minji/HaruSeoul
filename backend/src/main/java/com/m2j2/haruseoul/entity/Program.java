package com.m2j2.haruseoul.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "program")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "detail", length = 6000)
    private String detail;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date")
    @CreationTimestamp
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
    @UpdateTimestamp
    private Instant updateDate;

    @Column(name = "language")
    private String language;

    @Column(name = "start_time")
    private LocalTime startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reg_member_id")
    @JsonBackReference
    private Member member;

    @OneToMany(mappedBy = "program",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Image> images;

    @OneToMany(mappedBy = "program",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "program", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<CategoryProgram> categoryPrograms;

    @OneToMany(mappedBy = "program", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<PublishedProgram> publishedPrograms;

    @OneToMany(mappedBy = "program",cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Route> routes;

    @Column(name = "inclusion")
    private String inclusion;

    @Column(name = "exclusion")
    private String exclusion;

    @Column(name = "packing_list")
    private String packingList;

    @Column(name = "caution")
    private String caution;

    @Column(name = "requirement")
    private String requirement;

    @Column(name = "delete_date")
    private Instant deleteDate;
}
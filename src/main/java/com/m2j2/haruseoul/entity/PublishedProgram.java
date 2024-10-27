package com.m2j2.haruseoul.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "published_program")
public class PublishedProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    @JsonBackReference
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    @JsonBackReference
    private Status status;

    @Column(name = "group_size_current", nullable = false)
    private Integer groupSizeCurrent;

    // 진행일 ( 공개한 시간 x )
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "publishedProgram")
    @JsonManagedReference
    private List<Reservation> reservations;
}
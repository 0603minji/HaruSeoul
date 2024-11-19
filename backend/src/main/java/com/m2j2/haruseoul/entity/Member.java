package com.m2j2.haruseoul.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date", nullable = false)
    @CreationTimestamp
    private Instant regDate;

    @ColumnDefault("current_timestamp()")
    @Column(name = "update_date")
    @UpdateTimestamp
    private Instant updateDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "status")
    private String status;

    @Column(name = "profile_img_src")
    private String profileImgSrc;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Program> programs;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Review> reviews;

    @OneToMany(mappedBy = "member")
    @JsonManagedReference
    private List<Reservation> reservations;
}
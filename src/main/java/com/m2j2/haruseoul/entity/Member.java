package com.m2j2.haruseoul.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @ColumnDefault("current_timestamp()")
    @Column(name = "reg_date", nullable = false)
    private Instant regDate;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @OneToMany(mappedBy = "regMember")
    private List<Program> programs;
}
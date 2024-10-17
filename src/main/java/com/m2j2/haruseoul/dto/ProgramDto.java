package com.m2j2.haruseoul.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.entity.Review;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.List;

public class ProgramDto {

    private Long id;

    private String title;

    private String detail;

    private Instant regDate;

    private Instant endTime;

    private String status;

    private Integer price;

    private Integer groupSizeMax;

    private Integer groupSizeMin;

    private Float rating;

    private Instant updateDate;

    private String category1;

    private String category2;

    private String language;

    private Instant startTime;

    private Member member;

    private List<Image> images;

    private List<Review> reviews;

}

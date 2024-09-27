package com.m2j2.haruseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Route {
    private long id;
    private String title;
    private int order;
    private String address;
    private String transport;
    private String duration;
    private long programId;
}

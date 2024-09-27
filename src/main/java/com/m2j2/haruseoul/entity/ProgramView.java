package com.m2j2.haruseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProgramView {
    private Long id;
    private String title;
    private String category1;
    private String category2;
    private String status;
    private String thumbnail;
}

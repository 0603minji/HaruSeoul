package com.m2j2.haruseoul.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Published {
    private long id;
    private int groupSize;
    private String status;
    private Date date;
    private long programId;
}

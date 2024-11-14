package com.m2j2.haruseoul.host.program.dto;

import com.m2j2.haruseoul.entity.Image;
import com.m2j2.haruseoul.entity.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramCreateDto {

    private String title;
    private String detail;
    private Long regMemberId;
    private List<Long> categoryIds;
    private String language;
    private String startTimeHour;
    private String startTimeMinute;
    private String endTimeHour;
    private String endTimeMinute;
    private Integer groupSizeMax;
    private Integer groupSizeMin;
    private Integer price;
    private List<RouteCreateDto> routes;
    private String inclusion;
    private String exclusion;
    private String packingList; // 준비물
    private String caution; // 주의사항
    private String requirement; // 요청사항
    private String status;
//    private List<MultipartFile> images;
}

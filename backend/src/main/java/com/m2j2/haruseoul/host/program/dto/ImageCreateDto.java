package com.m2j2.haruseoul.host.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageCreateDto {

    private Long programId;
    private List<ImageDto> imageDtos;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageDto {
        private Integer order;
        private MultipartFile src;
    }
}

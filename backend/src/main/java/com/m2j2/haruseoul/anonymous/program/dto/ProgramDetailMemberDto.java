package com.m2j2.haruseoul.anonymous.program.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramDetailMemberDto {

    private Long hostId;

    private String hostName;
    private String hostNickname;
    private String hostProfileImg;

}

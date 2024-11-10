package com.m2j2.haruseoul.host.program.mapper;

import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.entity.CategoryProgram;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.entity.Route;
import com.m2j2.haruseoul.host.program.dto.ProgramListDto;
import com.m2j2.haruseoul.host.program.dto.RouteCreateDto;

import java.time.LocalTime;
import java.util.List;

public class ProgramMapper {
//    public static Program mapToEntity(ProgramDto programDto) {
//        return Program.builder()
//                .id(programDto.getId())
//                .title(programDto.getTitle())
//                .detail(programDto.getDetail())
//                .regDate(programDto.getRegDate())
//                .endTime(programDto.getEndTime())
//                .status(programDto.getStatus())
//                .price(programDto.getPrice())
//                .groupSizeMin(programDto.getGroupSizeMin())
//                .groupSizeMax(programDto.getGroupSizeMax())
//                .rating(programDto.getRating())
//                .updateDate(programDto.getUpdateDate())
//                .language(programDto.getLanguage())
//                .startTime(programDto.getStartTime())
//                .member(programDto.getMember())
//                .images(programDto.getImages())
//                .reviews(programDto.getReviews())
//                .build();
//    }

    public static ProgramListDto mapToDto(Program program) {

        String startTime = program.getStartTime().toString();
        String[] splitStartTime = startTime.split(":");
        String endTime = program.getEndTime().toString();
        String[] splitEndTime = endTime.split(":");

        String startTimeHour = splitStartTime[0];
        String startTimeMinute = splitStartTime[1];
        String endTimeHour = splitEndTime[0];
        String endTimeMinute = splitEndTime[1];

        return ProgramListDto.builder()
                .id(program.getId())
                .title(program.getTitle())
                .detail(program.getDetail())
                .regDate(program.getRegDate())
                .endTime(program.getEndTime())
                .startTime(program.getStartTime())
                .status(program.getStatus())
                .price(program.getPrice())
                .groupSizeMin(program.getGroupSizeMin())
                .groupSizeMax(program.getGroupSizeMax())
                .rating(program.getRating())
                .updateDate(program.getUpdateDate())
                .language(program.getLanguage())
                .memberId(program.getMember().getId())
                .categoryNames(
                        program.getCategoryPrograms()
                        .stream()
                        .map(CategoryProgram::getCategory)
                        .map(Category::getName)
                        .sorted()
                        .toList()
                )
//                .images(program.getImages())
//                .reviews(program.getReviews())
                .startTimeHour(startTimeHour)
                .startTimeMinute(startTimeMinute)
                .endTimeHour(endTimeHour)
                .endTimeMinute(endTimeMinute)
                .build();
    }


    //  route도 포함해서 ProgramListDto 생성
    public static ProgramListDto mapToDto(Program program, List<Route> routes) {
        ProgramListDto programListDto = mapToDto(program);
        List<RouteCreateDto> routeCreateDtos = routes.stream().map(route -> {
            int transportationDuration = localTimeToInteger(route.getTransportationDuration());

            LocalTime localTime = route.getStartTime() == null ? LocalTime.of(0, 0) : route.getStartTime();
            String[] split = localTime.toString().split(":");

            RouteCreateDto routeCreateDto = RouteCreateDto.builder()
                    .id(route.getId())
                    .order(route.getOrder())
                    .title(route.getTitle())
                    .description(route.getDescription())
                    .address(route.getAddress())
                    .transportationDuration(String.valueOf(transportationDuration)) //
                    .transportationId(route.getTransportation().getId())
                    .duration(localTimeToInteger(route.getDuration()))
                    .startTimeHour(split[0])
                    .startTimeMinute(split[1])
                    .build();
            return routeCreateDto;
        }).toList();

        programListDto.setRoute(routeCreateDtos);

        return programListDto;
    }


    public static void main(String[] args) {
        LocalTime localTime = LocalTime.of(14, 30);
        System.out.println(localTime);

        String[] split = localTime.toString().split(":");
        System.out.println(split[0]);
        System.out.println(split[1]);
    }

    public static int localTimeToInteger(LocalTime localTime) {
        if(localTime == null) {
            return 0;
        }

        String strLocalTime = localTime.toString();
        String[] split = strLocalTime.split(":");

        int hour = Integer.parseInt(split[0]) * 60;
        int minute = Integer.parseInt(split[1]);

        return (hour + minute);
    }
}

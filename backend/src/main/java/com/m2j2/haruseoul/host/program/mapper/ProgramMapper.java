package com.m2j2.haruseoul.host.program.mapper;

import com.m2j2.haruseoul.entity.*;
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
                .inclusion(program.getInclusion())
                .exclusion(program.getExclusion())
                .packingList(program.getPackingList())
                .caution(program.getCaution())
                .requirement(program.getRequirement())
                .images(program.getImages())
                .build();
    }


    //  route객체 리스트도 포함해서 ProgramListDto 생성
    //  프로그램과 그 프로그램의 경로(route) 리스트를 포함해서 ProgramListDto로 생성
//    public static ProgramListDto mapToDto(Program program, List<Route> routes) {
//        //  Program 객체를 먼저 ProgramListDto로 변환
//        ProgramListDto programListDto = mapToDto(program);
//        //  Route 객체 리스트(routes)를 RouteCreateDto 객체 리스트(routeCreateDtos)로 변환
//        //  각 Route 객체를 RouteCreateDto 필드에 맞게 매핑
//        List<RouteCreateDto> routeCreateDtos = routes.stream().map(route -> {
//            //  이동시간을 LocalTime 형태에서 int 형태로 변환 (분단위)
//            int transportationDuration = localTimeToInteger(route.getTransportationDuration());
//
//            //  경유지 시작시간이 null이면 00:00 으로 변환
//            LocalTime localTime = route.getStartTime() == null ? LocalTime.of(0, 0) : route.getStartTime();
//            //  경유지 시작시간을 String으로 전환하고 ":" 기준으로 분리하여 split[0]에 시간, split[1]에 분으로 나눔
//            String[] split = localTime.toString().split(":");
//
//            //  Route 객체의 필드들을 기반으로 새로운 RouteCreateDto로 빌드
//            RouteCreateDto routeCreateDto = RouteCreateDto.builder()
//                    .id(route.getId())
//                    .order(route.getOrder())
//                    .title(route.getTitle())
//                    .description(route.getDescription())
//                    .address(route.getAddress())
//                    .transportationDuration(String.valueOf(transportationDuration)) //
//                    .transportationId(route.getTransportation().getId())
//                    .transportationName(route.getTransportation().getName())
//                    .duration(localTimeToInteger(route.getDuration()))
//                    .startTimeHour(split[0])
//                    .startTimeMinute(split[1])
//                    .build();
//            return routeCreateDto;
//        }).toList();
//
//        //  ProgramListDto의 route필드에 RouteCreateDto 리스트를 설정
//        programListDto.setRoute(routeCreateDtos);
//
//        return programListDto;
//    }


    public static void main(String[] args) {
        LocalTime localTime = LocalTime.of(14, 30);
        System.out.println(localTime);

        String[] split = localTime.toString().split(":");
        System.out.println(split[0]);
        System.out.println(split[1]);
    }

    
    //  LocalTime 값을 분단위 정수(int)로 변환
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

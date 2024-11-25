//package com.m2j2.haruseoul.host.program.service;
//
//import java.time.LocalTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DefaultProgramServiceTest {
//
//
//    public static void main(String[] args) {
//        System.out.println(getLocalTimeByDuration("60"));
//        System.out.println(getLocalTimeByDuration("70"));
//        System.out.println(getLocalTimeByDuration("30"));
//        System.out.println(getLocalTimeByDuration("120"));
//        System.out.println(getLocalTimeByDuration("600"));
//        System.out.println(getLocalTimeByDuration("320"));
//        System.out.println(getLocalTimeByDuration("80"));
//        System.out.println(getLocalTimeByDuration("00"));
//        System.out.println(getLocalTimeByDuration(null));
//
//    }
//
//    private static LocalTime getLocalTimeByDuration(String duration) {
//        if(duration == null) return LocalTime.of(0, 0);
//        Integer durationTime = Integer.valueOf(duration);
//
//        int hour = durationTime / 60;
//        int minute = durationTime % 60;
//        return LocalTime.of(hour, minute);
//    }
//}
//        System.out.println(getLocalTimeByDuration("130"));

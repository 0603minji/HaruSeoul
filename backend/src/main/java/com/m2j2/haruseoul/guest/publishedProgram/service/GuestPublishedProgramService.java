package com.m2j2.haruseoul.guest.publishedProgram.service;


import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdateDto;
import com.m2j2.haruseoul.guest.publishedProgram.dto.GuestPublishedProgramUpdatedDto;

public interface GuestPublishedProgramService {
    GuestPublishedProgramUpdatedDto update(GuestPublishedProgramUpdateDto guestPublishedProgramUpdateDto);
}

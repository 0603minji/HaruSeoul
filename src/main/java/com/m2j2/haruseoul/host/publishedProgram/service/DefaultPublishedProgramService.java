package com.m2j2.haruseoul.host.publishedProgram.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.entity.PublishedProgram;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreateDto;
import com.m2j2.haruseoul.host.publishedProgram.dto.PublishedProgramCreatedDto;
import com.m2j2.haruseoul.repository.ProgramRepository;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultPublishedProgramService implements PublishedProgramService {

    ProgramRepository programRepository;
    PublishedProgramRepository publishedProgramRepository;

    public DefaultPublishedProgramService(ProgramRepository programRepository, PublishedProgramRepository publishedProgramRepository) {
        this.programRepository = programRepository;
        this.publishedProgramRepository = publishedProgramRepository;
    }

    @Override
    public PublishedProgramCreatedDto create(PublishedProgramCreateDto publishedProgramCreateDto) {

        List<PublishedProgram> publishedPrograms = new ArrayList<>();

        Program program = programRepository.findById(publishedProgramCreateDto.getProgramId())
                .orElseThrow(() -> new EntityNotFoundException("Program not found with ID: " + publishedProgramCreateDto.getProgramId()));

        for (LocalDate date : publishedProgramCreateDto.getDates()) {
            PublishedProgram publishedProgram = PublishedProgram.builder()
                    .program(program)
                    .date(date)
                    .build();
            publishedPrograms.add(publishedProgram);
        }

        List<PublishedProgram> savedPublishedPrograms = publishedProgramRepository.saveAll(publishedPrograms);
        PublishedProgramCreatedDto publishedProgramCreatedDto = PublishedProgramCreatedDto.builder()
                .build();
        return null;
    }
}

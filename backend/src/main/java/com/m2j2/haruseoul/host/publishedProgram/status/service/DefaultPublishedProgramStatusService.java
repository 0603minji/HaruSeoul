package com.m2j2.haruseoul.host.publishedProgram.status.service;

import com.m2j2.haruseoul.entity.Status;
import com.m2j2.haruseoul.host.publishedProgram.status.dto.PublishedProgramStatusDto;
import com.m2j2.haruseoul.host.publishedProgram.status.dto.PublishedProgramStatusResponseDto;
import com.m2j2.haruseoul.repository.StatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultPublishedProgramStatusService implements PublishedProgramStatusService {

    StatusRepository repository;
    ModelMapper modelMapper;

    public DefaultPublishedProgramStatusService(StatusRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PublishedProgramStatusResponseDto getList() {
        List<Status> statuses = repository.findAll();
        List<PublishedProgramStatusDto> statusDtos = statuses.stream()
                .map(status -> modelMapper.map(status, PublishedProgramStatusDto.class))
                .toList();
        return PublishedProgramStatusResponseDto.builder()
                .statusDtos(statusDtos)
                .build();
    }
}

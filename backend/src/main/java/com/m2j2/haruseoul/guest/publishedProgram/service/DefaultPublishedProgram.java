package com.m2j2.haruseoul.guest.publishedProgram.service;

import com.m2j2.haruseoul.guest.publishedProgram.dto.PublishedProgramDto;
import com.m2j2.haruseoul.repository.PublishedProgramRepository;
import org.springframework.transaction.annotation.Transactional;

public class DefaultPublishedProgram implements PublishedProgramService {

    private PublishedProgramRepository publishedProgramRepository;

    public DefaultPublishedProgram(PublishedProgramRepository publishedProgramRepository) {
        this.publishedProgramRepository = publishedProgramRepository;
    }

    // 공개된 프로그램에서 CurrentGroupSize 가져오고, reservation 에서 numberOfSize 빼주기, 근데 취소요청할때 계산값이 0이 되면 status 를 4로 바꾸기

    @Override
    @Transactional
    public PublishedProgramDto update(PublishedProgramDto publishedProgramDto) {
        return null;
    }
}

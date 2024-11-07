package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailImageDto;
import com.m2j2.haruseoul.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProgramDetailImageService implements ProgramDetailImageService {

    private final ImageRepository imageRepository;

    public DefaultProgramDetailImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public ProgramDetailImageDto getSrc(Long id) {
        List<String> src = imageRepository.findSrcByProgramId(id);

         ProgramDetailImageDto programDetailImageDto = ProgramDetailImageDto
                .builder()
                .imgSrc(src)
                .build();
         return programDetailImageDto;
    }
}

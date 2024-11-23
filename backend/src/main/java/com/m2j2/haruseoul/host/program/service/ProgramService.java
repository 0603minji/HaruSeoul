package com.m2j2.haruseoul.host.program.service;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProgramService {
    ProgramResponseDto getList(Long id, List<Long> programIds, List<Long> categoryIds,
                               List<String> statuses, int pageNum, int cardsPerPage);

    List<ProgramFilterListDto> getList(Long hostId, List<Long>pIds, List<String> statuses);
    List<ProgramTitle> getProgramTitles();
    Program create(ProgramCreateDto programCreateDto, List<MultipartFile> images);

    ProgramEditDto getProgram(Long id);

    Program update(ProgramUpdateDto programUpdateDto,List<MultipartFile> images);

    void delete(Long programId);

    Program statusCheck(Long pId);

    Program softDelete(Long pId);
}

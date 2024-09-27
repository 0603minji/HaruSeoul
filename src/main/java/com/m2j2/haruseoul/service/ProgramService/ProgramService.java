package com.m2j2.haruseoul.service.ProgramService;

import com.m2j2.haruseoul.entity.ProgramView;

import java.util.List;

public interface ProgramService {

    List<ProgramView> getList(Long hostId);
    List<ProgramView> getList(Long hostId, List<String> categories);
    List<ProgramView> getList(Long hostId, List<String> categories, List<Long> ids);
    List<ProgramView> getList(Long hostId, List<String> categories, List<Long> ids, List<String> statuses);
}

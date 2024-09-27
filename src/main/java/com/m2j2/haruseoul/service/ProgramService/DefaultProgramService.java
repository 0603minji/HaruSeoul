package com.m2j2.haruseoul.service.ProgramService;

import com.m2j2.haruseoul.entity.ProgramView;
import com.m2j2.haruseoul.repository.ProgramRepository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultProgramService implements ProgramService {

    @Autowired
    ProgramRepository programRepository;

    @Override
    public List<ProgramView> getList(Long hostId) {
        List<ProgramView> programs = programRepository.findAll(hostId, null, null, null);
        return programs;
    }

    @Override
    public List<ProgramView> getList(Long hostId, List<String> categories) {
        List<ProgramView> programs = programRepository.findAll(hostId, categories, null, null);
        return programs;
    }

    @Override
    public List<ProgramView> getList(Long hostId, List<String> categories, List<Long> ids) {
        List<ProgramView> programs = programRepository.findAll(hostId, categories, ids, null);
        return programs;
    }

    @Override
    public List<ProgramView> getList(Long hostId, List<String> categories, List<Long> ids, List<String> statuses) {
        List<ProgramView> programs = programRepository.findAll(hostId, categories, ids, statuses);
        return programs;
    }
}

package com.m2j2.haruseoul.repository.ProgramRepository;

import com.m2j2.haruseoul.entity.ProgramView;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProgramRepository {

    // host/program/list의 카드에 담을 필드들
    List<ProgramView> findAll(Long hostId, List<String> categories, List<Long> ids, List<String> statuses);
}

package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailCategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryProgramService {

    ProgramDetailCategoryDto getCategoryNames(Long id);

}

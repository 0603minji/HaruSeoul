package com.m2j2.haruseoul.anonymous.program.service;

import com.m2j2.haruseoul.anonymous.program.dto.ProgramDetailCategoryDto;
import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.repository.CategoryProgramRepository;
import com.m2j2.haruseoul.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("programsDetailCategoryService")
public class DefaultCategoryProgramService implements CategoryProgramService {

    private final CategoryRepository categoryRepository;
    private final CategoryProgramRepository categoryProgramRepository;

    public DefaultCategoryProgramService(CategoryProgramRepository categoryProgramRepository, CategoryRepository categoryRepository) {
        this.categoryProgramRepository = categoryProgramRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProgramDetailCategoryDto getCategoryNames(Long id) {

        List<Long> categoryIds = categoryProgramRepository.findCategoryIdsByProgramId(id);


        List<String> categoryNames = categoryRepository.findByIdIn(categoryIds)
                .stream()
                .map(Category::getName)
                .toList();

        ProgramDetailCategoryDto programDetailCategoryDto = new ProgramDetailCategoryDto();
        programDetailCategoryDto.setCategoryNames(categoryNames);

        return programDetailCategoryDto;
    }
}
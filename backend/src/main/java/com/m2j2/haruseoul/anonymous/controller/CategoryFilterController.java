package com.m2j2.haruseoul.anonymous.controller;

import com.m2j2.haruseoul.anonymous.dto.CategoryDto;
import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.anonymous.service.DefaultCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryFilterController {

    DefaultCategoryService categoryService;

    public CategoryFilterController(DefaultCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getList() {
        List<Category> categories = categoryService.getList();
        List<CategoryDto> categoryDtos = categories.stream().map(
                category -> {
                    return CategoryDto.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .build();
                }
        ).toList();
        return ResponseEntity.ok(categoryDtos);
    }
}

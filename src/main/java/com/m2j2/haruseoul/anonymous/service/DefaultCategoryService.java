package com.m2j2.haruseoul.anonymous.service;


import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCategoryService implements CategoryService {
    CategoryRepository categoryRepository;

    public DefaultCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<Category> getList() {
        return categoryRepository.findAll();
    }
}

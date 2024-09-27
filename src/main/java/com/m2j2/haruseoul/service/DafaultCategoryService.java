package com.m2j2.haruseoul.service;

import com.m2j2.haruseoul.entity.Category;
import com.m2j2.haruseoul.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DafaultCategoryService implements CategoryService {
    @Autowired
    CategoryRepository repository;

    @Override
    public List<Category> getList() {
        return repository.findAll();
    }
}

package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryRepository {
    public List<Category> findAll();
}

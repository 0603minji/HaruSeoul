package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIdIn(List<Long> ids);

    @Query("SELECT c FROM Category c WHERE c.id = :categoryId")
    Category findByCategoryId(@Param("categoryId")Long categoryId);

}

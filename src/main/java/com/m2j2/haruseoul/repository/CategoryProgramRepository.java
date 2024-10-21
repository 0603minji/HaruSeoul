package com.m2j2.haruseoul.repository;


import com.m2j2.haruseoul.entity.CategoryProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryProgramRepository extends JpaRepository<CategoryProgram, Long> {

    List<CategoryProgram> findByCategoryIdIn(List<Long> categoryIds);

}

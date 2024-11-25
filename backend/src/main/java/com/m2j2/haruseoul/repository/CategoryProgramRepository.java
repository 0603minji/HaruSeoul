package com.m2j2.haruseoul.repository;


import com.m2j2.haruseoul.entity.CategoryProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryProgramRepository extends JpaRepository<CategoryProgram, Long> {

    List<CategoryProgram> findByCategoryIdIn(List<Long> categoryIds);

    void deleteByProgramId(Long programId);

    @Query("SELECT cp.category.id FROM CategoryProgram cp WHERE cp.program.id = :programId")
    List<Long> findCategoryIdsByProgramId(@Param("programId") Long programId);

}

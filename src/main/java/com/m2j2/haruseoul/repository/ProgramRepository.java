package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Query("from Program where " +
            "(:cIds is null or categoryId in (:cIds))")
    Page<Program> findAll(@Param("username") String korName, @Param("cIds") List<Long> categoryIds, Pageable pageable);
}

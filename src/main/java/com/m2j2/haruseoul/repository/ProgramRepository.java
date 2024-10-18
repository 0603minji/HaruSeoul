package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("from Program p " +
            "where (:statuses is null or p.status = :statuses)" +
            "and (:pIds is null or p.id in :pIds)")
    Page<Program> findAll(@Param("pIds") List<Long> programIds, List<String> statuses, Pageable pageable);
}

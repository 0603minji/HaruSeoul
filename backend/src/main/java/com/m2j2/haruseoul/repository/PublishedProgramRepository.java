package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.PublishedProgram;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.m2j2.haruseoul.entity.Status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PublishedProgramRepository extends JpaRepository<PublishedProgram, Long> {
    @Query("from PublishedProgram pp " +
            "where pp.program.member.id in :mIds")
    List<PublishedProgram> findByHostIds(@Param("mIds") List<Long> memberIds);

    @Query("from PublishedProgram pp " +
            "where (:mIds is null or pp.program.member.id in :mIds) " +
            "and ((:start is null or :end is null) or pp.date between :start and :end) " +
            "and (:statusIds is null or pp.status.id in :statusIds) " +
            "and (:programIds is null or pp.program.id in :programIds)")
    Page<PublishedProgram> findAll(@Param("mIds") List<Long> memberIds, LocalDate start, LocalDate end, List<Long> statusIds, List<Long> programIds, Pageable pageable);


    @Query("SELECT pp.program.id FROM PublishedProgram pp " +
            "WHERE (:startDate IS NULL AND :endDate IS NULL OR " +
            "(pp.date >= :startDate OR :startDate IS NULL) AND " +
            "(pp.date <= :endDate OR :endDate IS NULL))")
    List<Long> findProgramIdsByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

}


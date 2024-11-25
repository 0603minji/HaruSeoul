package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.PublishedProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PublishedProgramRepository extends JpaRepository<PublishedProgram, Long> {
    @Query("from PublishedProgram pp " +
            "where pp.program.member.id in :mIds")
    List<PublishedProgram> findByHostIds(@Param("mIds") List<Long> memberIds);

    // 특정 프로그램 ID에 해당하며 status가 종료, 취소인것 말고는 공개 프로그램 모두 조회
    @Query("from PublishedProgram pp " +
            "where pp.program.id = :pId " +
            "and pp.status.id in (1, 2, 5, 6)")
    List<PublishedProgram> findByProgramId(@Param("pId") Long programId);

    @Query("from PublishedProgram pp " +
            "where (:mIds is null or pp.program.member.id in :mIds) " +
            "and ((:start is null or :end is null) or pp.date between :start and :end) " +
            "and (:statusIds is null or pp.status.id in :statusIds) " +
            "and (:programIds is null or pp.program.id in :programIds)")
    Page<PublishedProgram> findAll(@Param("mIds") List<Long> memberIds, LocalDate start, LocalDate end, List<Long> statusIds, List<Long> programIds, Pageable pageable);

    @Query("from PublishedProgram pp " +
            "where (:mIds is null or pp.program.member.id in :mIds) " +
            "and ((:start is null or :end is null) or pp.date between :start and :end) " +
            "and (:statusIds is null or pp.status.id in :statusIds) " +
            "and (:programIds is null or pp.program.id in :programIds)")
    List<PublishedProgram> findAllUnpaged(@Param("mIds") List<Long> memberIds, LocalDate start, LocalDate end, List<Long> statusIds, List<Long> programIds, Sort sort);

    @Query("SELECT pp.program.id FROM PublishedProgram pp " +
            "WHERE (:startDate IS NULL AND :endDate IS NULL OR " +
            "(pp.date >= :startDate OR :startDate IS NULL) AND " +
            "(pp.date <= :endDate OR :endDate IS NULL)) AND " +
            "(pp.status.id = 1 OR pp.status.id = 2)")
    List<Long> findProgramIdsByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);


    @Query("SELECT DISTINCT pp.program.id FROM PublishedProgram pp " +
            "WHERE pp.program.member.id = :hostId")
    List<Long> findDistinctProgramIdsByHostId(@Param("hostId") Long hostId);


    @Query("SELECT p.program.id " +
            "FROM PublishedProgram p " +
            "WHERE p.id = :publishedProgramId")
    Long findProgramIdByPublishedProgramId(@Param("publishedProgramId") Long publishedProgramId);

    Boolean existsByProgramId(Long programId);
    Boolean existsByProgramIdAndStatusIdIn(Long programId, List<Long> statusIds);

}


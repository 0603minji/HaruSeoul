package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("from Program p " +
            "where (:statuses is null or p.status in :statuses) " +
            "and (:pIds is null or p.id in :pIds)")
    Page<Program> findAll(
            @Param("pIds") List<Long> programIds,
            @Param("statuses") List<String> statuses,
            Pageable pageable
    );

    @Query("SELECT new com.m2j2.haruseoul.host.program.dto.ProgramTitle(p.id, p.title) FROM Program p ORDER BY p.title")
    List<ProgramTitle> findAllByOrderByTitle();

    @Query("from Program p " +
            "where (:hostId is null or p.member.id = :hostId)" +
            "and (:statuses is null or p.status in :statuses)" +
            "and (:pIds is null or p.id in :pIds)")
    List<Program> findAllList(Long hostId, @Param("pIds") List<Long> programIds, List<String> statuses);


    @Query("SELECT p FROM Program p " +
            "WHERE p.id IN :programIds " +
            "AND (:categoryIds IS NULL OR EXISTS " +
            "(SELECT 1 FROM CategoryProgram cp WHERE cp.program.id = p.id AND cp.category.id IN :categoryIds)) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
            "AND (:groupSizeMin IS NULL OR p.groupSizeMin >= :groupSizeMin) " +
            "AND (:groupSizeMax IS NULL OR p.groupSizeMax <= :groupSizeMax) " +
            "AND (:startTimeOption IS NULL OR " +
            "(CASE " +
            "    WHEN :startTimeOption = 1 THEN TRUE " +  // "전체" 옵션
            "    WHEN :startTimeOption = 2 THEN p.startTime < :morningTime " +  // "오전 12시 이전"
            "    WHEN :startTimeOption = 3 THEN p.startTime >= :morningTime AND p.startTime < :afternoonTime " +  // "오후 12시 ~ 오후 6시"
            "    WHEN :startTimeOption = 4 THEN p.startTime >= :afternoonTime " +  // "오후 6시 이후"
            "END)) " +
            "AND (:language IS NULL OR p.language = :language) " +
            "AND (:durationOption IS NULL OR " +
            "(CASE WHEN :durationOption = 1 THEN TIMESTAMPDIFF(HOUR, p.startTime, p.endTime) < 2 " +
            "WHEN :durationOption = 2 THEN TIMESTAMPDIFF(HOUR, p.startTime, p.endTime) BETWEEN 2 AND 4 " +
            "WHEN :durationOption = 3 THEN TIMESTAMPDIFF(HOUR, p.startTime, p.endTime) BETWEEN 4 AND 6 " +
            "WHEN :durationOption = 4 THEN TIMESTAMPDIFF(HOUR, p.startTime, p.endTime) BETWEEN 6 AND 8 " +
            "WHEN :durationOption = 5 THEN TIMESTAMPDIFF(HOUR, p.startTime, p.endTime) > 8 " +
            "END))"+
            "AND (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Program> findProgramsByFilters(@Param("programIds") List<Long> programIds,
                                        @Param("categoryIds") List<Long> categoryIds,
                                        @Param("title") String title,
                                        @Param("minPrice") Integer minPrice,
                                        @Param("maxPrice") Integer maxPrice,
                                        @Param("groupSizeMin") Integer groupSizeMin,
                                        @Param("groupSizeMax") Integer groupSizeMax,
                                        @Param("startTimeOption") Integer startTimeOption,
                                        @Param("morningTime") LocalTime morningTime,
                                        @Param("afternoonTime") LocalTime afternoonTime,
                                        @Param("language") String language,
                                        @Param("durationOption") Integer durationOption,
                                        Pageable pageable);


    @Query("SELECT p.member.id FROM Program p WHERE p.id = :id")
    Long findMemberIdByProgramId(@Param("id") Long programId);

    @Query("SELECT p.id FROM Program p WHERE p.member.id = :memberId")
    List<Long> findIdByMemberId(@Param("memberId") Long memberId);

    @Query("from Program p " +
            "where (p.deleteDate is null)" +
            "and (:statuses is null or p.status in :statuses) " +
            "and (:mId is null or p.member.id = :mId)" +
            "and (:pIds is null or p.id in :pIds)")
    Page<Program> findAllByMid(
            @Param("mId") Long id,
            @Param("pIds") List<Long> programIds,
            @Param("statuses") List<String> statuses,
            Pageable pageable
    );


    @Query("SELECT p FROM Program p WHERE p.id = :programId")
    Program findByProgramId(@Param("programId") Long programId);


    Program findTitleById(Long programId);
}




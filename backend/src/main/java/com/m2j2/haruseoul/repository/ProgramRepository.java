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
            "AND (:startTime IS NULL OR p.startTime >= :startTime) " +
            "AND (:language IS NULL OR p.language = :language)")
    Page<Program> findProgramsByFilters(@Param("programIds") List<Long> programIds,
                                        @Param("categoryIds") List<Long> categoryIds,
                                        @Param("minPrice") Integer minPrice,
                                        @Param("maxPrice") Integer maxPrice,
                                        @Param("groupSizeMin") Integer groupSizeMin,
                                        @Param("groupSizeMax") Integer groupSizeMax,
                                        @Param("startTime") LocalTime startTime,
                                        @Param("language") String language,
                                        Pageable pageable);

}




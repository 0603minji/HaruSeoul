package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("from Route rt " +
            "where rt.program.id = :pId and rt.order = 1")
    Route meetingSpotTitleByProgramId(@Param("pId") Long pId);

    void deleteByProgramId(Long programId);

    List<Route> findByProgramId(Long programId);
}

package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRepository extends JpaRepository<Route, Long> {
    @Query("select rt.title " +
            "from Route rt " +
            "where rt.program.id = :pId")
    String titleByProgramId(@Param("pId") Long pId);

    @Query("select rt.address " +
            "from Route rt " +
            "WHERE rt.program.id = :pId")
    String addressByProgramId(@Param("pId") Long pId);
}

package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.PublishedProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublishedProgramRepository extends JpaRepository<PublishedProgram, Long> {
    @Query("from PublishedProgram pp " +
            "where pp.program.member.id in :mIds")

    List<PublishedProgram> findByHostIds(@Param("mIds") List<Long> memberIds);

}

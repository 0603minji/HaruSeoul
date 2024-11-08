package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {


    @Query("SELECT i.src FROM Image i WHERE i.program.id = :programId")
    List<String> findSrcByProgramId(@Param("programId") Long programId);


}
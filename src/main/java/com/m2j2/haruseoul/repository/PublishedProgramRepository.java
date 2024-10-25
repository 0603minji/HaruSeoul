package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.PublishedProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface PublishedProgramRepository extends JpaRepository<PublishedProgram, Long> {
    public List<PublishedProgram> findByDateIn(List<LocalDate> dates);
}

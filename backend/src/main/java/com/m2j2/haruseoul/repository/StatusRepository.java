package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findAll();
}

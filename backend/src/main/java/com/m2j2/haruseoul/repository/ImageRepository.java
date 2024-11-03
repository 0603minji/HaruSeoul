package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

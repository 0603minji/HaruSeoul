package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}

package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("select COUNT(r)" +
            " from Review r " +
            "where r.program.member.id = :mId")
    Long countByMemberId(@Param("mId") Long mId);

    @Query("select SUM(r.rating)" +
            " from Review r " +
            "where r.program.member.id = :mId")
    Long sumRatingByMemberId(@Param("mId") Long mId);
}

package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserId(String userId);
    Member findByUserIdAndUserPwd(String userId, String userPwd);

    @Query("SELECT m.name FROM Member m WHERE m.id = :id")
    String getNameById(@Param("id") Long id);

    @Query("SELECT m.name FROM Member m WHERE m.id = :memberId")
    String findNameById(@Param("memberId") Long memberId);

}
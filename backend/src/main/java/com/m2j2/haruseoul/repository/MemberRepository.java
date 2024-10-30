package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserId(String userId);
}

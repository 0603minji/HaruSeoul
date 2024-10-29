package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Member;
import com.m2j2.haruseoul.entity.Program;
import com.m2j2.haruseoul.host.program.dto.ProgramTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query("from Program p " +
            "where (:statuses is null or p.status in :statuses)" +
            "and (:pIds is null or p.id in :pIds) ")
    //  host 1사람에 대한 프로그램만 나와야함
    //  예) p=3 선택시 3페이지가 나오기
    Page<Program> findAll(@Param("pIds") List<Long> programIds, List<String> statuses, Pageable pageable);


    //  위에 쿼리문으로 공용화하기
    //  위에서 반환타입만 Page<Progrma>에서 List<Program>으로 바꿔 써보기
    @Query("SELECT new com.m2j2.haruseoul.host.program.dto.ProgramTitle(p.id, p.title) FROM Program p ORDER BY p.title")
    List<ProgramTitle> findAllByOrderByTitle();

    @Query("from Program p " +
            "where (:hostId is null or p.member.id = :hostId)" +
            "and (:statuses is null or p.status in :statuses)" +
            "and (:pIds is null or p.id in :pIds)")
    List<Program> findAll2(Long hostId, @Param("pIds") List<Long> programIds, List<String> statuses);
}

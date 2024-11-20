package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("from Reservation r " +
            "where (:sIds is null or :isDeleted = true or r.publishedProgram.status.id in :sIds)" +
            "and (:mIds is null or r.member.id in :mIds)" +
            "and (:isDeleted = false or r.deleteDate is not null)")
    Page<Reservation> findAll(@Param("sIds") List<Long> sIds, @Param("mIds") List<Long> mIds,@Param("isDeleted") Boolean isDeleted, Pageable pageable);

    @Query("from Reservation r " +
            "where (r.publishedProgram.id = :ppId)" +
            "and (r.cancelMethod = 3 or r.deleteDate is null)")
    List<Reservation> findByPpIdByCancelMethod(Long ppId);
}
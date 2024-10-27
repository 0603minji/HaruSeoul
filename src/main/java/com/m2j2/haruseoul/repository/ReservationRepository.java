package com.m2j2.haruseoul.repository;

import com.m2j2.haruseoul.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("from Reservation r " +
            "where (:sId = null or r.publishedProgram.status.id = :sId)")
    Page<Reservation> findReservationsByStatusId(@Param("sId") Long sId, Pageable pageable);

}

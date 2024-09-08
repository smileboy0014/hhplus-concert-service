package com.hhplus.hhplusconcertservice.infra;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import static com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo.ReservationStatus;

public interface ReservationJpaRepository extends JpaRepository<ConcertReservationEntity, Long> {

    List<ConcertReservationEntity> findAllByUserId(Long userId);

    List<ConcertReservationEntity> findAllByStatusIs(ReservationStatus status);

    boolean existsByConcertIdAndConcertDateIdAndSeatNumberAndStatusIsNot(Long concertId, Long concertDateId, int seatNumber, ReservationStatus cancel);
}

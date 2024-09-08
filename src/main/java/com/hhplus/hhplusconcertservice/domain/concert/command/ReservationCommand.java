package com.hhplus.hhplusconcertservice.domain.concert.command;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertDate;
import com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo;
import com.hhplus.hhplusconcertservice.domain.concert.Seat;

public class ReservationCommand {
    public record Create(
            Long concertId,
            Long concertDateId,
            int seatNumber,
            Long userId
    ) {

        public ConcertReservationInfo from(Seat seat, ConcertDate concertDate) {
            return ConcertReservationInfo.builder()
                    .concertId(concertId())
                    .concertDateId(concertDateId())
                    .userId(userId())
                    .seatId(seat.getSeatId())
                    .concertName(concertDate.getConcert().getName())
                    .concertDate(concertDate.getConcertDate())
                    .seatNumber(seat.getSeatNumber())
                    .seatPrice(seat.getPrice())
                    .build();
        }
    }


    public record Delete(
            Long reservationId,
            Long userId

    ) {
    }

    public record Complete(
            Long reservationId
    ) {
    }
}

package com.hhplus.hhplusconcertservice.interfaces.controller.reservation.dto;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo;
import com.hhplus.hhplusconcertservice.domain.concert.command.ReservationCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

import static com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo.ReservationStatus;

public class ReservationDto {

    @Builder(toBuilder = true)
    public record Request(@NotNull Long concertId,
                          @NotNull Long concertDateId,
                          @NotNull int seatNumber,
                          @NotNull Long userId) {

        public ReservationCommand.Create toCreateCommand() {
            return new ReservationCommand.Create(concertId, concertDateId, seatNumber, userId);
        }
    }


    @Builder(toBuilder = true)
    public record Response(Long reservationId, ReservationStatus status,
                           ReservationConcertInfo concertInfo
    ) {

        public static Response of(ConcertReservationInfo reservation) {
            return Response.builder()
                    .reservationId(reservation.getReservationId())
                    .status(reservation.getStatus())
                    .concertInfo(new ReservationConcertInfo(
                            reservation.getConcertId(),
                            reservation.getConcertDateId(),
                            reservation.getSeatId(),
                            reservation.getConcertName(),
                            reservation.getConcertDate(),
                            reservation.getSeatNumber(),
                            reservation.getSeatPrice()
                    ))
                    .build();

        }

        public record ReservationConcertInfo(
                Long concertId,
                Long concertDateId,
                Long seatId,
                String concertName,
                String concertDate,
                int seatNumber,
                BigDecimal seatPrice
        ) {

        }
    }
}

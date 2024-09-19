package com.hhplus.hhplusconcertservice.interfaces.controller.concert.dto;

import com.hhplus.hhplusconcertservice.domain.concert.Seat;
import lombok.Builder;

import java.math.BigDecimal;

public class ConcertSeatDto {

    @Builder(toBuilder = true)
    public record Response(Long seatId, int seatNumber, Seat.TicketClass ticketClass, BigDecimal price) {

        public static Response of(Seat seat) {
            return Response.builder()
                    .seatId(seat.getSeatId())
                    .seatNumber(seat.getSeatNumber())
                    .ticketClass(seat.getTicketClass())
                    .price(seat.getPrice())
                    .build();

        }
    }
}

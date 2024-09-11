package com.hhplus.hhplusconcertservice.interfaces.controller.concert.dto;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertDate;
import lombok.Builder;

public class ConcertDateDto {

    @Builder(toBuilder = true)
    public record Response(Long concertDateId, String place,
                           String concertDate, boolean isAvailable) {
        public static Response of(ConcertDate concertDate) {
            return Response.builder()
                    .concertDateId(concertDate.getConcertDateId())
                    .place(concertDate.getPlace().getName())
                    .concertDate(concertDate.getConcertDate())
                    .isAvailable(concertDate.getIsAvailable())
                    .build();

        }
    }
}

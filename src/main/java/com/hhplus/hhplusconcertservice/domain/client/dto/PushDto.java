package com.hhplus.hhplusconcertservice.domain.client.dto;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertDate;
import lombok.Builder;

public class PushDto {

    @Builder(toBuilder = true)
    public record Request(Long userId) {
    }
}

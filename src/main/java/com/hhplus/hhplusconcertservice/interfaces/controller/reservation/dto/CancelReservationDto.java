package com.hhplus.hhplusconcertservice.interfaces.controller.reservation.dto;

import com.hhplus.hhplusconcertservice.domain.concert.command.ReservationCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CancelReservationDto {

    @Builder(toBuilder = true)
    public record Request(@NotNull Long userId) {

        public ReservationCommand.Delete toDeleteCommand(Long reservationId) {
            return new ReservationCommand.Delete(reservationId, userId);
        }
    }
}

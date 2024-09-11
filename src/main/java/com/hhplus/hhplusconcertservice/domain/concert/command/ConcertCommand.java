package com.hhplus.hhplusconcertservice.domain.concert.command;

public class ConcertCommand {
    public record Create(
            String concertName
    ) {
    }
}

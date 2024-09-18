package com.hhplus.hhplusconcertservice.domain.outbox.command;

import com.hhplus.hhplusconcertservice.domain.outbox.Outbox;

public class OutboxCommand {
    public record Create(
            String messageId,
            Outbox.EventStatus status,
            String payload) {

        public Outbox toDomain() {
            return Outbox.builder()
                    .messageId(messageId)
                    .status(status)
                    .payload(payload)
                    .build();
        }
    }
}

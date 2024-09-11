package com.hhplus.hhplusconcertservice.interfaces.consumer;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertService;
import com.hhplus.hhplusconcertservice.domain.concert.command.ReservationCommand;
import com.hhplus.hhplusconcertservice.domain.concert.event.PaymentEvent;
import com.hhplus.hhplusconcertservice.support.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.hhplus.hhplusconcertservice.support.config.KafkaTopicConfig.KafkaConstants.PAYMENT_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConcertConsumer {

    private final ConcertService concertService;

    @KafkaListener(topics = PAYMENT_TOPIC, groupId = "hhplus-01")
    public void sendPaymentInfo(String key, String message) {
        log.info("[KAFKA] :: CONSUMER:: Received PAYMENT_TOPIC, key: {}, payload: {}", key, message);

        PaymentEvent payload = JsonUtils.toObject(message, PaymentEvent.class);

        concertService.completeReservation(new ReservationCommand.Complete(payload.getReservationInfo().getReservationId()));
    }
}

package com.hhplus.hhplusconcertservice.interfaces.consumer;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertService;
import com.hhplus.hhplusconcertservice.domain.concert.command.ReservationCommand;
import com.hhplus.hhplusconcertservice.domain.concert.event.PaymentEvent;
import com.hhplus.hhplusconcertservice.support.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static com.hhplus.hhplusconcertservice.support.config.KafkaTopicConfig.KafkaConstants.PAYMENT_TOPIC;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConcertConsumer {

    private final ConcertService concertService;

    @KafkaListener(topics = PAYMENT_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void handlePaymentEvent(String key, String message) {
        log.info("[KAFKA] :: CONSUMER:: Received PAYMENT_TOPIC, key: {}, payload: {}", key, message);

        PaymentEvent payload = JsonUtils.toObject(message, PaymentEvent.class);

        // 결제 내역이 생성 되었을 경우
        if (payload != null && payload.getStatus().equals(PaymentEvent.EventConstants.NEW)) {
            concertService.completeReservation(ReservationCommand.Complete.toCommand(payload)); //예약 최종 완료
        }

        // 유저 포인트 차감이 실패한 경우
        if (payload != null && payload.getStatus().equals(PaymentEvent.EventConstants.DEDUCTION_FAILED)) {
            concertService.failReservation(Long.valueOf(payload.getReservationId()));
        }

//        ack.acknowledge(); //수동으로 offset 커밋
    }
}

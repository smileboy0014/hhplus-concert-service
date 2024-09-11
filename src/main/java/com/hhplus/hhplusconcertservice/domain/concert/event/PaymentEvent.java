package com.hhplus.hhplusconcertservice.domain.concert.event;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class PaymentEvent extends ApplicationEvent {

    public PaymentEvent() {
        super("");
    }


    private ConcertReservationInfo reservationInfo;
    private String messageId;
    private String paymentId;
    private String token;

    public PaymentEvent(Object source, ConcertReservationInfo reservationInfo,
                        String paymentId, String token) {
        super(source);
        this.reservationInfo = reservationInfo;
        this.paymentId = paymentId;
        this.token = token;
    }
}
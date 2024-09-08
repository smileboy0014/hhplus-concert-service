package com.hhplus.hhplusconcertservice.domain.concert.event;

import com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class ConcertEvent extends ApplicationEvent {

    public ConcertEvent() {
        super("");
    }


    private ConcertReservationInfo reservationInfo;

    public ConcertEvent(Object source, ConcertReservationInfo reservationInfo) {
        super(source);
        this.reservationInfo = reservationInfo;
    }

}

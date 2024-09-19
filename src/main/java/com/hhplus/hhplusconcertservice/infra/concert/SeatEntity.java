package com.hhplus.hhplusconcertservice.infra.concert;

import com.hhplus.hhplusconcertservice.domain.concert.Seat;
import com.hhplus.hhplusconcertservice.infra.common.model.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.hhplus.hhplusconcertservice.domain.concert.Seat.SeatStatus;
import static com.hhplus.hhplusconcertservice.domain.concert.Seat.TicketClass;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "seat", indexes = {
        @Index(name = "IDX_SEAT_COVERING", columnList = "concert_date_id, status, seat_id, seat_number, price, created_at, updated_at, ticket_class, version"),
        @Index(name = "IDX_SEAT_STATUS", columnList = "concert_date_id, status")})
public class SeatEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_date_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ConcertDateEntity concertDateInfo;

    private int seatNumber;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private SeatStatus status; // available, unavailable

    @Enumerated(EnumType.STRING)
    private TicketClass ticketClass; // S > A > B > C

    @CreatedDate
    private LocalDateTime createdAt;

    @Version
    private int version; //낙관적 락 적용

    public static SeatEntity from(Seat seat) {

        return SeatEntity.builder()
                .seatId(seat.getSeatId() != null ? seat.getSeatId() : null)
                .concertDateInfo(ConcertDateEntity.toEntity(seat.getConcertDateInfo()))
                .seatNumber(seat.getSeatNumber())
                .price(seat.getPrice())
                .status(seat.getStatus())
                .ticketClass(seat.getTicketClass())
                .version(seat.getVersion())
                .createdAt(seat.getCreatedAt())
                .build();
    }

    public Seat toDomain() {

        return Seat.builder()
                .seatId(seatId)
                .concertDateInfo(concertDateInfo.toDomain())
                .seatNumber(seatNumber)
                .price(price)
                .status(status)
                .ticketClass(ticketClass)
                .version(version)
                .createdAt(createdAt)
                .build();
    }
}

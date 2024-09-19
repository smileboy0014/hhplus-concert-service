package com.hhplus.hhplusconcertservice.domain.concert;

import com.hhplus.hhplusconcertservice.domain.client.PushClient;
import com.hhplus.hhplusconcertservice.domain.client.dto.PushDto;
import com.hhplus.hhplusconcertservice.domain.common.exception.CustomException;
import com.hhplus.hhplusconcertservice.domain.common.exception.ErrorCode;
import com.hhplus.hhplusconcertservice.domain.concert.command.ConcertCommand;
import com.hhplus.hhplusconcertservice.domain.concert.command.ReservationCommand;
import com.hhplus.hhplusconcertservice.domain.concert.event.ConcertEvent;
import com.hhplus.hhplusconcertservice.domain.concert.event.PaymentEvent;
import com.hhplus.hhplusconcertservice.infra.concert.ReservationJpaRepository;
import com.hhplus.hhplusconcertservice.interfaces.common.dto.ApiResultResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.hhplus.hhplusconcertservice.domain.common.exception.ErrorCode.*;
import static com.hhplus.hhplusconcertservice.domain.concert.ConcertReservationInfo.ReservationStatus.TEMPORARY_RESERVED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ConcertValidator concertValidator;
    private final ApplicationEventPublisher publisher;



    /**
     * 콘서트 정보를 요청하면 콘서트 정보를 반환한다.
     *
     * @return 콘서트 정보를 반환한다.
     */
    @Caching(cacheable = {
            @Cacheable(cacheManager = "l1LocalCacheManager", value = "concerts", key = "#pageable.pageNumber"),
            @Cacheable(cacheManager = "l2RedisCacheManager", value = "concerts", key = "#pageable.pageNumber")
    })
    @Transactional(readOnly = true)
    public Page<Concert> getConcerts(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("createdAt").descending());

        Page<Concert> concerts = concertRepository.getConcerts(sortedPageable);

        return concerts
                .map(concert -> {
                    List<ConcertDate> concertDates = concertRepository.getConcertDates(concert.getConcertId());
                    return Concert.builder()
                            .concertId(concert.getConcertId())
                            .name(concert.getName())
                            .concertDates(concertDates)
                            .build();
                });
    }

    /**
     * 콘서트 상세 정보를 요청하면 콘서트 상세 정보를 반환한다.
     *
     * @param concertId concertId 정보
     * @return 콘서트 상세 정보 반환
     */
    @Transactional(readOnly = true)
    public Concert getConcert(Long concertId) {
        List<ConcertDate> concertDates = concertRepository.getConcertDates(concertId);
        concertValidator.existConcert(concertId, concertDates);

        return Concert
                .builder()
                .concertId(concertDates.get(0).getConcert().getConcertId())
                .name(concertDates.get(0).getConcert().getName())
                .concertDates(concertDates)
                .build();
    }

    /**
     * 콘서트 정보를 저장합니다.
     *
     * @return 콘서트 정보를 반환한다.
     */
    @Caching(evict = {
            @CacheEvict(value = "concerts", allEntries = true, cacheManager = "l1LocalCacheManager"),
            @CacheEvict(value = "concerts", allEntries = true, cacheManager = "l2RedisCacheManager"),
    })

    public Concert saveConcert(ConcertCommand.Create command) {
        Concert concert = Concert.builder().name(command.concertName()).build();
        Optional<Concert> savedConcert = concertRepository.saveConcert(concert);

        if (savedConcert.isEmpty()) throw new CustomException(CONCERT_IS_NOT_FOUND, CONCERT_IS_NOT_FOUND.getMsg());

        return savedConcert.get();
    }

    /**
     * 콘서트 예약 가능한 날짜를 요청하면 콘서트 예약 날짜 정보를 반환한다.
     *
     * @param concertId concertId 정보
     * @return 콘서트 예약 날짜 정보를 반환한다.
     */
    @Transactional(readOnly = true)
    public List<ConcertDate> getAvailableConcertDates(Long concertId) {
        List<ConcertDate> concertDates = concertRepository.getConcertDates(concertId);
        concertValidator.existAvailableConcertDates(concertId, concertDates);

        return concertDates.stream()
                .map(concertDate -> {
                    boolean available = concertRepository.existAvailableSeats(concertDate.getConcertDateId());
                    return ConcertDate.builder()
                            .concertDateId(concertDate.getConcertDateId())
                            .place(concertDate.getPlace())
                            .concertDate(concertDate.getConcertDate())
                            .isAvailable(available)
                            .build();
                }).toList();
    }

    /**
     * 예약 가능한 좌석을 요청하면 예약 가능한 좌석 정보를 반환한다.
     *
     * @param concertDateId concertDateId 정보
     * @return 예약 가능한 좌석 정보를 반환한다.
     */
    @Transactional(readOnly = true)
    public List<Seat> getAvailableSeats(Long concertDateId) {
        List<Seat> availableSeats = concertRepository.getAvailableSeats(concertDateId);
        concertValidator.existAvailableSeats(concertDateId, availableSeats);

        return availableSeats;
    }

    /**
     * 좌석 예약을 요청하면 예약 완료 정보를 반환한다.
     *
     * @param command concertId, concertDateId, seatNumber, userId 정보
     * @return 예약 완료 정보를 반환한다.
     */
    @Transactional
    public ConcertReservationInfo reserveSeat(ReservationCommand.Create command) {
        // 1 이미 예약이 있는지 확인
        boolean checkedReservation = concertRepository.checkAlreadyReserved(command.concertId(), command.concertDateId(),
                command.seatNumber());
        concertValidator.checkAlreadyReserved(checkedReservation, command.concertDateId(), command.seatNumber());
        // 2. concertDate 정보 조회
        ConcertDate availableDate = concertRepository.getAvailableDates(command.concertDateId(),
                command.concertId()).orElseThrow(() -> new CustomException(CONCERT_DATE_IS_NOT_FOUND,
                "예약 가능한 콘서트 날짜가 존재하지 않습니다. [concertDate: %d]".formatted(command.concertDateId())));
        // 3. 좌석 점유
        Seat availableSeat = concertRepository.getAvailableSeats(command.concertDateId(),
                command.seatNumber()).orElseThrow(()->new CustomException(SEAT_IS_NOT_FOUND,"예약 가능한 좌석이 존재하지 않습니다."));
        availableSeat.occupy();
        concertRepository.saveSeat(availableSeat);
        // 4. 예약 테이블 저장
        ConcertReservationInfo reservationInfo = command.from(availableSeat, availableDate);
        ConcertReservationInfo savedReservation = concertValidator.checkSavedReservation(concertRepository.saveReservation(reservationInfo), "예약에 실패하였습니다");
        // 5. 예약에 성공하면 kakao로 push 알람 설정
        publisher.publishEvent(new ConcertEvent(this, reservationInfo));

        return savedReservation;
    }

    /**
     * 예약 내역을 요청하면 유저의 예약 정보를 반환한다.
     *
     * @param userId userId 정보
     * @return 유저의 예약 정보를 반환한다.
     */
    @Transactional(readOnly = true)
    public List<ConcertReservationInfo> getMyReservations(Long userId) {
        return concertRepository.getMyReservations(userId);
    }

    /**
     * 예약을 취소한다.
     *
     * @param command reservationId 정보
     */
    @Transactional
    public ConcertReservationInfo cancelReservation(ReservationCommand.Delete command) {
        // 1. 예약 조회
        Optional<ConcertReservationInfo> reservation = concertRepository.getReservation(command.reservationId());
        ConcertReservationInfo reservationInfo = concertValidator.checkExistReservation(reservation,
                "취소할 예약 내역이 존재하지 않습니다");
        // 2. 예약 취소
        reservationInfo.cancel();
        Optional<ConcertReservationInfo> cancelReservation = concertRepository.saveReservation(reservationInfo);

        return concertValidator.checkSavedReservation(cancelReservation, "예약 취소에 실패하였습니다.");
    }

    /**
     * 좌석 점유를 해지한다.
     *
     * @param seatId seat 정보
     */
    @Transactional
    public void cancelOccupiedSeat(Long seatId) {
        // 좌석 정보 조회
        Optional<Seat> seat = concertRepository.getSeat(seatId);
        // 좌석 점유 취소
        Seat seatInfo = concertValidator.checkExistSeat(seat, "좌석 예약을 취소할 좌석이 존재하지 않습니다.");
        seatInfo.cancel();
        concertRepository.saveSeat(seatInfo);
    }

    /**
     * 예약을 완료한다.
     *
     * @param command reservation 정보
     */
    @Transactional
    public void completeReservation(ReservationCommand.Complete command) {
        try {
            ConcertReservationInfo reservation = concertRepository.getReservation(Long.valueOf(command.reservationId()))
                    .orElseThrow(() -> new CustomException(RESERVATION_IS_NOT_FOUND, "예약 완료할 예약 내역이 존재하지 않습니다"));

            //예약 완료로 상태 변경
            reservation.complete();

            // 예약 완료 저장
            concertRepository.saveReservation(reservation)
                    .orElseThrow(() -> new CustomException(RESERVATION_IS_FAILED, "예약 완료에 실패하였습니다"));

            // 유저 포인트 차감을 위한 메세지 발생
            publisher.publishEvent(new PaymentEvent(this, command.reservationId(), command.userId(),
                    command.paymentId(), command.token(), command.amount(),
                    PaymentEvent.EventConstants.RESERVATION_COMPLETED));

        } catch (Exception e) {
            log.error(e.getMessage());
            // 예약 완료 실패의 경우 실패 이벤트 발행
            publisher.publishEvent(new PaymentEvent(this, command.reservationId(), command.userId(),
                    command.paymentId(), command.token(), command.amount(),
                    PaymentEvent.EventConstants.RESERVATION_FAILED));
        }
    }

    @Transactional
    public void failReservation(Long reservationId) {

        ConcertReservationInfo reservation = concertRepository.getReservation(reservationId)
                .orElseThrow(() -> new CustomException(RESERVATION_IS_NOT_FOUND, "예약 완료할 예약 내역이 존재하지 않습니다"));
        reservation.fail();
        concertRepository.saveReservation(reservation);
    }

    /**
     * 좌석을 계속 점유할 수 있는지 확인한다.
     */
    @Transactional
    public void checkOccupiedSeat(Long reservationId) {
        Optional<ConcertReservationInfo> reservation = concertRepository.getReservation(reservationId);
        if (reservation.isEmpty() || !reservation.get().getStatus().equals(TEMPORARY_RESERVED)) {
            throw new CustomException(ErrorCode.RESERVATION_IS_ALREADY_CANCEL_OR_COMPLETE, "이미 완료되었거나 취소된 예약입니다.");
        }
        // 예약 상태 취소로 변경
        reservation.get().cancel();
        concertRepository.saveReservation(reservation.get());
        // 좌석 점유 취소(다시 예약 가능 상태로 변경)
        Optional<Seat> seat = concertRepository.getSeat(reservation.get().getSeatId());
        Seat seatInfo = concertValidator.checkExistSeat(seat, "좌석 정보가 존재하지 않습니다");
        seatInfo.cancel();

        concertRepository.saveSeat(seatInfo);
    }


}

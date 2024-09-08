package com.hhplus.hhplusconcertservice.domain.concert;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertMonitor {

    private RBlockingQueue<ConcertReservationInfo> tempReservationQueue;
    private RDelayedQueue<ConcertReservationInfo> delayedReservationQueue;
    private final ConcertService concertService;
    private final RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        cancelOccupiedSeatListener();

        tempReservationQueue = redissonClient.getBlockingQueue("tempReservationQueue");
        delayedReservationQueue = redissonClient.getDelayedQueue(tempReservationQueue);
    }

    public void reserveSeat(ConcertReservationInfo savedReservation) {
        // 예약 요청을 지연 큐에 추가, 5분 후에 처리되도록 설정
        delayedReservationQueue.offer(savedReservation, 5, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        delayedReservationQueue.destroy();
    }

    /**
     * 좌석 점유 해지 시간이 되면 동작한다.
     */
    private void cancelOccupiedSeatListener() {
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 내가 설정한 시간이 지나서 요소를 사용할 수 있을 때 까지 대기
                    if (tempReservationQueue != null) {
                        ConcertReservationInfo item = tempReservationQueue.take();
                        // 예약 상태가 임시 예약이면 예약 취소
                        concertService.checkOccupiedSeat(item.getReservationId());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}

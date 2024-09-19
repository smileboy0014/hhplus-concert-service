package com.hhplus.hhplusconcertservice.domain.client.listener;

import com.hhplus.hhplusconcertservice.domain.client.PushClient;
import com.hhplus.hhplusconcertservice.domain.client.dto.PushDto;
import com.hhplus.hhplusconcertservice.domain.concert.event.ConcertEvent;
import com.hhplus.hhplusconcertservice.interfaces.common.dto.ApiResultResponse;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientListener {

    private final CircuitBreakerFactory circuitBreakerFactory;
    private final PushClient pushClient;
    private final RetryRegistry retryRegistry;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onConcertEvent(ConcertEvent event) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("pushServiceCircuitBreaker");
        Retry retry = retryRegistry.retry("pushServiceRetry");
        try {
            Retry.decorateRunnable(retry, circuitBreaker.run(() -> {
                PushDto.Request request = new PushDto.Request(event.getReservationInfo().getUserId());
                ApiResultResponse<String> stringApiResultResponse = pushClient.sendPushNotification(request);
                log.info("[PUSH] push response is {}, success!!!!", stringApiResultResponse.data());
                return null;
            })).run();
        } catch (Exception e) {
            // 추가적인 예외 처리
            log.error("[PUSH] KAKAO push is failed!!!!!! {}", e.getMessage());
        }

        // 써드파티 서비스가 죽어도 내 서비스에는 영향이 안가게 함
        // 써드파티 서비스의 지연시간이 길어지는 경우에도 내 서비스에는 지장이 없게 설정 가능
        // 최대 Circuit 재시도 횟수도 설정 가능

    }
}

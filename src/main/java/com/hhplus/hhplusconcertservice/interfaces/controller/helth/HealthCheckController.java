package com.hhplus.hhplusconcertservice.interfaces.controller.helth;

import com.hhplus.hhplusconcertservice.domain.client.PushClient;
import com.hhplus.hhplusconcertservice.domain.concert.ConcertService;
import com.hhplus.hhplusconcertservice.interfaces.common.dto.ApiResultResponse;
import com.hhplus.hhplusconcertservice.interfaces.controller.concert.dto.ConcertDateDto;
import com.hhplus.hhplusconcertservice.interfaces.controller.concert.dto.ConcertDto;
import com.hhplus.hhplusconcertservice.interfaces.controller.concert.dto.ConcertSeatDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping

public class HealthCheckController {


    /**
     * healthCheck 용도
     *
     * @return ApiResultResponse 콘서트 목록을 반환한다.
     */
    @GetMapping("/healthCheck")
    public ApiResultResponse<String> getHealthCheck() {
        return ApiResultResponse.ok("Hello world!");
    }

}

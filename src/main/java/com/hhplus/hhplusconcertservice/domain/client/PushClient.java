package com.hhplus.hhplusconcertservice.domain.client;

import com.hhplus.hhplusconcertservice.domain.client.dto.PushDto;
import com.hhplus.hhplusconcertservice.interfaces.common.dto.ApiResultResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pushService", url = "${push.service.url}")
public interface PushClient {

    @PostMapping("/kakao/push")
    ApiResultResponse<String> sendPushNotification(@RequestBody PushDto.Request request);

}

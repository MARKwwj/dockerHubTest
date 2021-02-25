package com.magic.gateway.rmi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-server")
public interface AuthServerService {

    @PostMapping(value = "/token/auth")
    boolean auth(@RequestBody String token);
}

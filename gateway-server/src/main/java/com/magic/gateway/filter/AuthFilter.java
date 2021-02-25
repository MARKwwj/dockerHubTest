package com.magic.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.gateway.rmi.feign.AuthServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.magic.gateway.GatewayConstants.TOKEN;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthServerService authServerService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> headerList = headers.get(TOKEN);
        if (headerList == null) {
            throw new RuntimeException("非法请求");
        }
        String token = headerList.get(0);
        // 请求身份认证服务器，校验token
        if (!TokenUtil.verify(token)) {
            return fastFinish(exchange,"登录已过期，请重新登录");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> fastFinish(ServerWebExchange exchange, String msg) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String fastResult = JSONUtil.toJsonStr(JsonResult.success(msg));
        DataBuffer dataBuffer = response.bufferFactory().allocateBuffer().write(fastResult.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}

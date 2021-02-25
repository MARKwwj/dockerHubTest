package com.magic.gateway.routes;

import cn.hutool.core.util.StrUtil;
import com.magic.gateway.GatewayConstants;
import com.magic.gateway.SpringContextUtil;
import com.magic.gateway.handlers.GatewayDataHandler;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class ResponseBodyHandler implements RewriteFunction<String, String> {
    private final Map<String, String> route;

    public ResponseBodyHandler(Map<String, String> route) {
        this.route = route;
    }

    @Override
    public Publisher<String> apply(ServerWebExchange serverWebExchange, String body) {
        String encrypted = route.get("force-body-crypted");
        if ("true".equals(encrypted)) {
            String encryptType = route.get("body-crypt-type");
            if (StrUtil.isBlank(encryptType)) {
                encryptType = "zlib-aes";
            }
            GatewayDataHandler gatewayHandler = SpringContextUtil.getBean(encryptType);
            String newBody = gatewayHandler.handle(body, true);
            return Mono.just(newBody);
        }
        else {
            ServerHttpRequest request = serverWebExchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            encrypted = headers.getFirst(GatewayConstants.BODY_ENCRYPTED_HEADER);
            if ("true".equals(encrypted)) {
                String encryptType = headers.getFirst(GatewayConstants.BODY_ENCRYPTED_TYPE_HEADER);
                if (StrUtil.isBlank(encryptType)) {
                    encryptType = "zlib-aes";
                }
                GatewayDataHandler gatewayHandler = SpringContextUtil.getBean(encryptType);
                String newBody = gatewayHandler.handle(body, true);
                return Mono.just(newBody);
            }
        }
        return Mono.just(body);
    }
}

package com.magic.gateway.routes;

import cn.hutool.core.util.StrUtil;
import com.magic.gateway.handlers.GatewayDataHandler;
import com.magic.gateway.GatewayConstants;
import com.magic.gateway.SpringContextUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

public class RequestPathHandler implements GatewayFilter {
    private final Map<String, String> route;

    public RequestPathHandler(Map<String, String> route) {
        this.route = route;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String pathEncrypted = route.get("force-path-crypted");
        ServerWebExchange newExchange = exchange;
        if ("true".equals(pathEncrypted)) {
            String pathEncryptedType = route.get("path-crypt-type");
            if (pathEncryptedType == null || pathEncryptedType.isEmpty()) {
                pathEncryptedType = "aes";
            }
            newExchange = refreshServerWebExchange(exchange, pathEncryptedType);
        }
        else {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            pathEncrypted = headers.getFirst(GatewayConstants.PATH_ENCRYPTED_HEADER);
            if ("true".equals(pathEncrypted)) {
                String pathEncryptedType = route.get(GatewayConstants.PATH_ENCRYPTED_TYPE_HEADER);
                if (StrUtil.isBlank(pathEncryptedType)) {
                    pathEncryptedType = "aes";
                }
                newExchange = refreshServerWebExchange(exchange, pathEncryptedType);
            }
        }
        return chain.filter(newExchange);
    }

    private ServerWebExchange refreshServerWebExchange(ServerWebExchange exchange, String pathEncryptedType) {
        GatewayDataHandler gatewayHandler = SpringContextUtil.getBean(pathEncryptedType);
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getPath().value();
        requestPath = gatewayHandler.handle(requestPath.substring(1), false);
        ServerHttpRequest newRequest = request.mutate().path(requestPath).build();
        return exchange.mutate().request(newRequest).build();
    }
}

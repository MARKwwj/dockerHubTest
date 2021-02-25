package com.magic.gateway.routes;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.gateway.configs.GatewayConfig;
import com.magic.gateway.utils.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Map;

@Configuration
public class GatewayRouter {
    @Autowired
    private GatewayConfig gatewayConfig;

    @Bean
    public RouteLocator requestBodyHandlerRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (Map<String, String> route : gatewayConfig.getRoutes()) {
            if (!route.get("path").equals("/config_server/**")) {
                routes.route(
                        route.get("id"),
                        predicateSpec -> predicateSpec
                                .path(route.get("path"))
                                .filters(f -> f
                                        .modifyRequestBody(String.class, String.class, (exchange, body) -> {
                                            if (StrUtil.isNotBlank(body)) {
                                                body = ApiUtil.aesDecrypt(body);
                                                JSONObject jsonObject = JSONUtil.parseObj(body);
                                                jsonObject.remove("timeStamp");
                                                body = jsonObject.toString();
                                            } else {
                                                body = "";
                                            }
                                            return Mono.just(body);
                                        })
                                        .modifyResponseBody(String.class, String.class, (exchange, body) -> {
                                            if (StrUtil.isNotBlank(body)) {
                                                body = ApiUtil.aesEncrypt(body);
                                            } else {
                                                body = "";
                                            }
                                            return Mono.just(body);
                                        }))
                                .uri(route.get("uri"))
                );
            } else {
                routes.route(
                        route.get("id"),
                        predicateSpec -> predicateSpec
                                .path(route.get("path"))
                                .uri(route.get("uri")));
            }
        }
        return routes.build();
    }
}

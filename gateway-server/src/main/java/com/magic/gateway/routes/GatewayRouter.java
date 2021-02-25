package com.magic.gateway.routes;

import com.magic.gateway.GatewayConstants;
import com.magic.gateway.configs.GatewayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Map;

@Configuration
public class GatewayRouter {
    @Autowired
    private GatewayConfig gatewayConfig;

    @Bean
    public RouteLocator requestBodyHandlerRoutes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        for (Map<String, String> route : gatewayConfig.getRoutes()) {
            routes.route(
                    route.get("id"),
                    predicateSpec -> predicateSpec
                            // 兼容老版本，不再兼容时注释掉本行，去掉新版本注释
                            //.header(route.get("id") != "UseCrypt" ? GatewayConstants.ROUTE_HEADER : "UseCrypt", route.get("id") != "UseCrypt" ? route.get("id") : null)
                            //TODO 新版本
                            .header(GatewayConstants.ROUTE_HEADER, route.get("id"))
                            .filters(
                                    gatewayFilterSpec -> gatewayFilterSpec
                                            .filters(new RequestPathHandler(route))
                                            .modifyRequestBody(
                                                    String.class,
                                                    String.class,
                                                    MediaType.APPLICATION_JSON_VALUE,
                                                    new RequestBodyHandler(route))
                                            .modifyResponseBody(
                                                    String.class,
                                                    String.class,
                                                    MediaType.APPLICATION_JSON_VALUE,
                                                    new ResponseBodyHandler(route))
                            )
                            .uri(route.get("uri"))
            );
        }
        return routes.build();
    }
}

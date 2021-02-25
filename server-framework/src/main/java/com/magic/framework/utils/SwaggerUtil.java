package com.magic.framework.utils;

import com.magic.framework.properties.SwaggerProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

public class SwaggerUtil {
    public static Docket createDocket(SwaggerProperties swaggerProperties, List<Parameter> parameters) {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(parameters)
                .enable(swaggerProperties.getEnabled())
                .apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitle())
                        .version(swaggerProperties.getVersion())
                        .build()
                )
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build();
    }
}

package com.magic.video.common.config;

import com.magic.framework.properties.SwaggerProperties;
import com.magic.framework.utils.SwaggerUtil;
import com.magic.framework.utils.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final SwaggerProperties swaggerProperties;

    public SwaggerConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket customDocket() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("X-Token")
                .description("Token令牌")
                .required(true)
                .allowEmptyValue(false)
                .parameterType("header")
                .modelRef(new ModelRef("string"))
                .scalarExample(TokenUtil.generateTokenAndSaveRedis(42971L,1))
                .build());
        return SwaggerUtil.createDocket(swaggerProperties, parameters);
    }
}

package com.magic.search.common.filter;

import com.magic.framework.filter.BodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean registerAuthFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new BodyFilter());
        registration.addUrlPatterns("/*");
        registration.setName("bodyFilter");
        registration.setOrder(1);
        return registration;
    }
}

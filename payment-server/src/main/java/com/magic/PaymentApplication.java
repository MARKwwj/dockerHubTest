package com.magic;

import com.magic.framework.ServerContextInitializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.magic.payment.mapper")
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(PaymentApplication.class);
        springApplication.addInitializers(new ServerContextInitializer());
        springApplication.run(args);
    }
}

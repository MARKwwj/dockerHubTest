package com.magic;

import com.magic.framework.ServerContextInitializer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.magic.sms.mapper")
public class SmsApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SmsApplication.class);
        springApplication.addInitializers(new ServerContextInitializer());
        springApplication.run(args);
    }
}

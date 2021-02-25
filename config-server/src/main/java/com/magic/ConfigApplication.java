package com.magic;

import com.magic.framework.ServerContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ConfigApplication {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ConfigApplication.class);
        springApplication.addInitializers(new ServerContextInitializer());
        springApplication.run(args);
    }
}

package com.example.gachonhub;

import com.example.gachonhub.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AppProperties.class)
public class GachonHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(GachonHubApplication.class, args);
    }

}

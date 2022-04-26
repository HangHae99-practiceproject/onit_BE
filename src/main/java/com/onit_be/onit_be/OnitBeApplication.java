package com.onit_be.onit_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OnitBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnitBeApplication.class, args);
    }

}

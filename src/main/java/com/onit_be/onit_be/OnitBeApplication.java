package com.onit_be.onit_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession
public class OnitBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnitBeApplication.class, args);
    }

}

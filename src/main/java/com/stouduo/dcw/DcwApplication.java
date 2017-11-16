package com.stouduo.dcw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DcwApplication {

    public static void main(String[] args) {
        SpringApplication.run(DcwApplication.class, args);
    }
}

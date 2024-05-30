package com.ll.slog2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Slog2Application {

    public static void main(String[] args) {
        SpringApplication.run(Slog2Application.class, args);
    }

}

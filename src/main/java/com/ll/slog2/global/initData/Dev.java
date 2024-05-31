package com.ll.slog2.global.initData;

import com.ll.slog2.global.app.AppConfig;
import com.ll.slog2.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class Dev {
    @Bean
    @Order(5)
    ApplicationRunner initDev() {
        return args -> {
            String backUrl = AppConfig.getSiteBackUrl();
            Ut.cmd.run("curl -o apiV1.json -k " + backUrl + "/v3/api-docs/apiV1");
            Ut.cmd.run("bash -c 'npx openapi-typescript apiV1.json -o ./front/src/lib/backend/apiV1/schema.d.ts'");
            Ut.cmd.run("bash -c 'rm -f apiV1.json'");
        };
    }
}
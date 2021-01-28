package com.example.exchangerate;

import com.example.exchangerate.parser.WebParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@Slf4j
@SpringBootApplication
public class ExchangeRateApplication {

    public static void main(String[] args) {
        log.debug("Application started");
        ApiContextInitializer.init();
        SpringApplication.run(ExchangeRateApplication.class, args);
    }

}

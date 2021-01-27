package com.example.exchangerate;

import com.example.exchangerate.parser.WebParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication
public class ExchangeRateApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(ExchangeRateApplication.class, args);
    }

}

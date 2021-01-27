package com.example.exchangerate.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRate {
    private Currency currency;
    private String sellRate;
    private String buyRate;

    @Override
    public String toString() {
        return currency + " Покупка: " + buyRate + " Продажа: " + sellRate;
    }
}

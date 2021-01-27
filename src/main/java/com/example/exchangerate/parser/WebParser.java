package com.example.exchangerate.parser;

import com.example.exchangerate.model.Currency;
import com.example.exchangerate.model.ExchangeRate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebParser {

    public static List<ExchangeRate> ParsePrivat() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://privatbank.ua/ru").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements rows = doc.getElementsByTag("tr");

        Elements currencyElements = new Elements();
        currencyElements.add(rows.get(1));
        currencyElements.add(rows.get(2));
        currencyElements.add(rows.get(3));

        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        currencyElements.forEach(tableElement -> {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setCurrency(Currency.valueOf(tableElement.child(0).text()));
            exchangeRate.setBuyRate(tableElement.child(2).text());
            exchangeRate.setSellRate(tableElement.child(3).text());
            exchangeRateList.add(exchangeRate);
                }
        );
        return exchangeRateList;
    }

    public static List<ExchangeRate> ParceOshad() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.oschadbank.ua/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ExchangeRate> exchangeRateList = new ArrayList<>();

        ExchangeRate exchangeRateUSD = new ExchangeRate(Currency.USD,
                doc.getElementsByAttributeValue("class", "sell-USD").get(0).text(),
                doc.getElementsByAttributeValue("class", "buy-USD").get(0).text());
        ExchangeRate exchangeRateEUR = new ExchangeRate(Currency.EUR,
                doc.getElementsByAttributeValue("class", "sell-EUR").get(0).text(),
                doc.getElementsByAttributeValue("class", "buy-EUR").get(0).text());
        ExchangeRate exchangeRateRUB = new ExchangeRate(Currency.RUB,
                doc.getElementsByAttributeValue("class", "sell-RUB").get(0).text(),
                doc.getElementsByAttributeValue("class", "buy-RUB").get(0).text());

        exchangeRateList.add(exchangeRateEUR);
        exchangeRateList.add(exchangeRateUSD);
        exchangeRateList.add(exchangeRateRUB);
        return exchangeRateList;
    }
    public static List<ExchangeRate> ParseMoney() {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://money24.kharkov.ua/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements rows = doc.getElementsByAttributeValue("id", "tab1");
        List<ExchangeRate> exchangeRateList = new ArrayList<>();
        ExchangeRate exchangeRateUSD = new ExchangeRate(Currency.USD, rows.get(0).child(0).child(2).text(), rows.get(0).child(0).child(1).text());
        ExchangeRate exchangeRateEUR = new ExchangeRate(Currency.EUR, rows.get(0).child(1).child(2).text(), rows.get(0).child(1).child(1).text());
        ExchangeRate exchangeRateRUB = new ExchangeRate(Currency.RUB, rows.get(0).child(3).child(2).text(), rows.get(0).child(3).child(1).text());

        exchangeRateList.add(exchangeRateEUR);
        exchangeRateList.add(exchangeRateUSD);
        exchangeRateList.add(exchangeRateRUB);
        return exchangeRateList;
    }
}

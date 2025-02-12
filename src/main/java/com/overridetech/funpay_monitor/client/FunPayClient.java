package com.overridetech.funpay_monitor.client;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FunPayClient {

    private final static String FUN_PAY_POE2_DIVINES = "https://funpay.com/chips/209/";



    public static void getHtml() throws IOException {
        Document doc = Jsoup.connect(FUN_PAY_POE2_DIVINES).userAgent("Mozilla").get();
        Element element = doc.getElementsByClass("form-control showcase-filter-input").get(0);
        System.out.println(element.toString());


        doc.getAllElements().forEach(System.out::println);
        System.out.println();
    }


    public static void main(String[] args) throws IOException {
        getHtml();
    }
}

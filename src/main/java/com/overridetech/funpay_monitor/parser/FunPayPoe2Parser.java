package com.overridetech.funpay_monitor.parser;

import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.stream.Stream;

public class FunPayPoe2Parser {
    public static FunPayPoe2Offer parseHtmlToFunPayPoe2Offer(String html) {

        Document doc = Jsoup.parse(html);

        String ref = parseElement(doc, "a", "href");
        String item = parseElement(doc, "div.tc-side");
        String unit = parseElement(doc, "div.tc-price > div > span.unit");
        String price = parseElement(doc, "div.tc-price").replace(unit, "").trim();
        String stock = parseElement(doc, "div.tc-amount");
        String seller = parseElement(doc, "div.media-user-name");
        Boolean isOnline = parseIsOnline(doc);
        String server = parseElement(doc, "div.tc-server");
        String rating = parseElement(doc, "div.rating-stars", "class");
        String reviews = parseElement(doc, "span.rating-mini-count");
        String experience = parseElement(doc, "div.media-user-info");

        return FunPayPoe2Offer.builder()
                .ref(ref)
                .item(item)
                .price(price)
                .stock(stock)
                .seller(seller)
                .isOnline(isOnline)
                .server(server)
                .rating(rating)
                .reviews(reviews)
                .experience(experience)
                .build();
    }

    private static Boolean parseIsOnline(Document document) {
        return Stream.of(document.selectFirst("div.media.user"))
                .filter(Objects::nonNull)
                .map(e -> e.attr("class"))
                .map(e -> e.contains("online"))
                .findFirst().orElse(false);
    }

    private static String parseElement(Document document, String cssQuery, String attributeKey) {
        return Stream.of(document.selectFirst(cssQuery))
                .filter(Objects::nonNull)
                .map(e -> e.attr(attributeKey))
                .filter(Strings::isNotBlank)
                .findFirst().orElse(null);
    }

    private static String parseElement(Document document, String cssQuery) {
        return Stream.of(document.selectFirst(cssQuery))
                .filter(Objects::nonNull)
                .map(Element::text)
                .filter(Strings::isNotBlank)
                .findFirst().orElse(null);
    }
}

package com.overridetech.funpay_monitor.parser;

import com.overridetech.funpay_monitor.dto.BaseOffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class Parser {

    public static final String TAG_ITEM = "div.tc-side";
    public static final String TAG_UNIT = "div.tc-price > div > span.unit";
    public static final String TAG_PRICE = "div.tc-price";
    public static final String TAG_STOCK = "div.tc-amount";
    public static final String TAG_SELLER = "div.media-user-name";
    public static final String TAG_SERVER = "div.tc-server";
    public static final String TAG_RATING = "div.rating-stars";
    public static final String TAG_REVIEWS = "span.rating-mini-count";
    public static final String TAG_EXPERIENCE = "div.media-user-info";
    public static final String TAG_ONLINE = "div.media.media-user";
    public static final String TAG_HREF = "href";
    public static final String TAG_CLASS = "class";
    public static final String TAG_A = "a";



    public BaseOffer parseHtmlToFunPayPoe2Offer(String html, String category) {

        try {
            Document doc = Jsoup.parse(html);

            String ref = parseElement(doc, TAG_A, TAG_HREF);

            String item = parseElement(doc, TAG_ITEM);

            String unit = parseElement(doc, TAG_UNIT);
            String price = parseElement(doc, TAG_PRICE).replace(unit, "")
                    .replace(" ", "").trim();
            String stock = parseElement(doc, TAG_STOCK).replace(" ", "").trim();
            String seller = parseElement(doc, TAG_SELLER);
            Boolean isOnline = parseIsOnline(doc);
            String server = parseElement(doc, TAG_SERVER);
            String rating = parseElement(doc, TAG_RATING, TAG_CLASS);
            String reviews = parseElement(doc, TAG_REVIEWS).replace(" ", "").trim();
            String experience = parseElement(doc, TAG_EXPERIENCE);

            return BaseOffer.builder()
                    .ref(ref)
                    .category(category)
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
        } catch (Exception e) {
            log.error("что-то обосралось при парсе: {}", e.getMessage());
            return null;
        }
    }

    private static Boolean parseIsOnline(Document document) {
        return Stream.of(document.selectFirst(TAG_ONLINE))
                .filter(Objects::nonNull)
                .map(e -> e.attr(TAG_CLASS))
                .map(e -> e.contains("online"))
                .findFirst().orElse(false);
    }

    private static String parseElement(Document document, String cssQuery, String attributeKey) {
        return Stream.of(document.selectFirst(cssQuery))
                .filter(Objects::nonNull)
                .map(e -> e.attr(attributeKey))
                .filter(Strings::isNotBlank)
                .findFirst().orElse("");
    }

    private static String parseElement(Document document, String cssQuery) {
        return Stream.of(document.selectFirst(cssQuery))
                .filter(Objects::nonNull)
                .map(Element::text)
                .filter(Strings::isNotBlank)
                .findFirst().orElse("");
    }

}

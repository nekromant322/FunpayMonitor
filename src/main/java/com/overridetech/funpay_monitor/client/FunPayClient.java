package com.overridetech.funpay_monitor.client;


import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FunPayClient {

    private final static String FUN_PAY_POE2_DIVINES = "https://funpay.com/chips/209/";
    private final static String FUN_PAY_POE2_TABLE_OFFERS = "tc table-hover table-clickable showcase-table tc-sortable tc-lazyload";

    @Value("${funpay.currency-cookies}")
    private String currencyCookie;


    public List<String> getHtmls(String url) throws IOException {

        Document doc = Jsoup.connect(url).userAgent("Mozilla").cookie("cy", currencyCookie).get();

        Elements elements = doc.getElementsByClass(FUN_PAY_POE2_TABLE_OFFERS);

        List<String> htmls = elements.getFirst().childNodes().stream()
                .filter(node -> node.getClass() != TextNode.class)
                .skip(1)
                .map(Node::toString)
                .toList();

        return htmls;
    }
}

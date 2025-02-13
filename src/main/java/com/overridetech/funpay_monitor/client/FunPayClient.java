package com.overridetech.funpay_monitor.client;


import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.parser.FunPayPoe2Parser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FunPayClient {

    private final static String FUN_PAY_POE2_DIVINES = "https://funpay.com/chips/209/";
    private final static String FUN_PAY_POE2_TABLE_OFFERS = "tc table-hover table-clickable showcase-table tc-sortable tc-lazyload";


    public static List<FunPayPoe2Offer> getHtml() throws IOException {
        Document doc = Jsoup.connect(FUN_PAY_POE2_DIVINES).userAgent("Mozilla").get();

        Elements elements = doc.getElementsByClass(FUN_PAY_POE2_TABLE_OFFERS);

        List<String> htmls = elements.getFirst().childNodes().stream()
                .filter(node -> node.getClass() != TextNode.class)
                .skip(1)
                .map(Node::toString)
                .toList();

        List<FunPayPoe2Offer> offers = htmls.stream().map(FunPayPoe2Parser::parseHtmlToFunPayPoe2Offer).toList();

        return offers;
    }





    public static void main(String[] args) throws IOException {
        getHtml();
    }
}

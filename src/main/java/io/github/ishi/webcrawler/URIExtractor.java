package io.github.ishi.webcrawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Set;

public class URIExtractor {
    public Set<String> extract(String input) {
        Document document = Jsoup.parse(input);
        Set<String> result = new HashSet<>();
        result.addAll(document.select("a").eachAttr("href"));
        result.addAll(document.select("img").eachAttr("src"));
        result.addAll(document.select("script").eachAttr("src"));
        return result;
    }
}

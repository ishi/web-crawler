package io.github.ishi.webcrawler.infrastructure.html;

import io.github.ishi.webcrawler.core.content.URIExtractor;
import io.github.ishi.webcrawler.core.model.ExtractedUri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class HtmlURIExtractor implements URIExtractor {
    @Override
    public Set<ExtractedUri> extract(String input) {
        Document document = Jsoup.parse(input);
        Set<ExtractedUri> result = new HashSet<>();
        result.addAll(document.select("a").eachAttr("href").stream().map(ExtractedUri::internalLink).collect(toSet()));
        result.addAll(document.select("img").eachAttr("src").stream().map(ExtractedUri::staticResource).collect(toSet()));
        result.addAll(document.select("script").eachAttr("src").stream().map(ExtractedUri::staticResource).collect(toSet()));
        return result;
    }
}

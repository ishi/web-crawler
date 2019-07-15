package io.github.ishi.webcrawler.infrastructure.html;

import io.github.ishi.webcrawler.core.content.URIExtractor;
import io.github.ishi.webcrawler.core.model.ExtractedUri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class HtmlURIExtractor implements URIExtractor {
    @Override
    public Set<ExtractedUri> extract(String input) {
        Document document = Jsoup.parse(input);
        return Stream
                .concat(collectUrls(document), collectStaticContent(document))
                .collect(toSet());
    }

    private Stream<ExtractedUri> collectUrls(Document document) {
        return document.select("a").eachAttr("href").stream()
                .filter(uri -> !uri.startsWith("mailto"))
                .map(uri -> uri.replaceAll(" ", "%20"))
                .map(ExtractedUri::internalLink);
    }

    private Stream<ExtractedUri> collectStaticContent(Document document) {
        return Stream.concat(
                document.select("img").eachAttr("src").stream(),
                document.select("script").eachAttr("src").stream())
                .map(ExtractedUri::staticResource);
    }
}

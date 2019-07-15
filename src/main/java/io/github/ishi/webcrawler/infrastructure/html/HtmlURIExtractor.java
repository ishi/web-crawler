package io.github.ishi.webcrawler.infrastructure.html;

import io.github.ishi.webcrawler.core.content.URIExtractor;
import io.github.ishi.webcrawler.core.model.ExtractedUri;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class HtmlURIExtractor implements URIExtractor {
    private static final Logger logger = LoggerFactory.getLogger(HtmlURIExtractor.class);

    @Override
    public Set<ExtractedUri> extract(String input) {
        try {
            Document document = Jsoup.parse(input);
            return Stream
                    .concat(collectUrls(document), collectStaticContent(document))
                    .collect(toSet());
        } catch (Exception ex) {
            logger.error("Problem processing content", ex);
            return Set.of();
        }
    }

    private Stream<ExtractedUri> collectUrls(Document document) {
        return document.select("a").eachAttr("href").stream()
                .filter(uri -> !uri.startsWith("mailto:"))
                .filter(uri -> !uri.startsWith("data:"))
                .filter(uri -> !uri.startsWith("javascript:"))
                .map(uri -> uri.trim().replaceAll(" ", "%20"))
                .map(ExtractedUri::internalLink);
    }

    private Stream<ExtractedUri> collectStaticContent(Document document) {
        return Stream.concat(
                document.select("img").eachAttr("src").stream(),
                document.select("script").eachAttr("src").stream())
                .filter(uri -> !uri.startsWith("data:"))
                .map(ExtractedUri::staticResource);
    }
}

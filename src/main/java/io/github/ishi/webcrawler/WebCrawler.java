package io.github.ishi.webcrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

public class WebCrawler {
    private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

    private final DataProvider provider;
    private final URIExtractor extractor;

    private final LinkedList<String> toProcess = new LinkedList<>();
    private final Set<String> visitedLinks = new HashSet<>();

    public WebCrawler(DataProvider provider, URIExtractor extractor) {
        this.provider = provider;
        this.extractor = extractor;
    }

    public void analyze(String baseUrl) {
        toProcess.add(baseUrl);

        while (!toProcess.isEmpty()) {
            logger.debug("URIs to process {}", toProcess);
            String candidate = toProcess.removeFirst();
            try {
                UriNormalizer normalizer = new UriNormalizer(candidate);

                if (visitedLinks.contains(candidate)) continue;
                visitedLinks.add(candidate);

                Set<ExtractedUri> links = provider.getContent(candidate)
                        .map(extractor::extract)
                        .orElse(Collections.emptySet())
                        .stream()
                        .map(normalizer::normalize)
                        .collect(toSet());

                toProcess.addAll(
                        links.stream()
                                .filter(onlyInternalUris())
                                .map(ExtractedUri::getUri)
                                .collect(toSet()));

                logger.debug("Visited URIs {}", visitedLinks);
            } catch (URISyntaxException e) {
                logger.error("Problem parsing URI", e);
            }
        }
    }

    private Predicate<? super ExtractedUri> onlyInternalUris() {
        return extractedUri -> extractedUri.getType() == URIType.INTERNAL_LINK;
    }

}

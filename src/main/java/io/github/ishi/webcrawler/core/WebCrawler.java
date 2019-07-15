package io.github.ishi.webcrawler.core;

import io.github.ishi.webcrawler.core.content.URIExtractor;
import io.github.ishi.webcrawler.core.input.DataProvider;
import io.github.ishi.webcrawler.core.model.ExtractedUri;
import io.github.ishi.webcrawler.core.model.URIType;
import io.github.ishi.webcrawler.core.model.uri.UriNormalizer;
import io.github.ishi.webcrawler.core.output.DataOutput;
import io.github.ishi.webcrawler.core.output.OutputFormat;
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
    private final OutputFormat formatter;
    private final DataOutput output;

    private final LinkedList<String> toProcess = new LinkedList<>();
    private final Set<String> visitedLinks = new HashSet<>();
    private final Set<ExtractedUri> collectedUris = new HashSet<>();

    public WebCrawler(DataProvider provider, URIExtractor extractor, OutputFormat formatter, DataOutput output) {
        this.provider = provider;
        this.extractor = extractor;
        this.formatter = formatter;
        this.output = output;
    }

    public void analyze(String baseUrl) {
        toProcess.add(baseUrl);

        while (!toProcess.isEmpty()) {
            String candidate = toProcess.removeFirst();
            if (visitedLinks.contains(candidate)) continue;

            processUri(candidate);
        }
        output.accept(formatter.format(collectedUris));
    }

    private void processUri(String uri) {
        logger.info("Processing URI {}", uri);
        try {
            Set<ExtractedUri> foundUris = extractUrisFromPage(uri);
            toProcess.addAll(selectUrisForProcessing(foundUris));
            collectedUris.addAll(foundUris);
            visitedLinks.add(uri);
        } catch (URISyntaxException e) {
            logger.error("Problem parsing URI", e);
        }
    }

    private Set<ExtractedUri> extractUrisFromPage(String uri) throws URISyntaxException {
        return provider.getContent(uri)
                .map(extractor::extract)
                .orElse(Collections.emptySet())
                .stream()
                .map(new UriNormalizer(uri)::normalize)
                .collect(toSet());
    }

    private Set<String> selectUrisForProcessing(Set<ExtractedUri> foundUris) {
        return foundUris.stream()
                .filter(onlyInternalUris())
                .map(ExtractedUri::getUri)
                .collect(toSet());
    }

    private Predicate<? super ExtractedUri> onlyInternalUris() {
        return extractedUri -> extractedUri.getType() == URIType.INTERNAL_LINK;
    }

}

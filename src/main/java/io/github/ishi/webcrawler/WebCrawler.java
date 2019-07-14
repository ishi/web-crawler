package io.github.ishi.webcrawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

public class WebCrawler {

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

        System.out.println("-------start ------");
        while (!toProcess.isEmpty()) {
            String candidate = toProcess.removeFirst();

            if(visitedLinks.contains(candidate)) continue;
            visitedLinks.add(candidate);

            Set<ExtractedUri> links = provider.getContent(candidate)
                    .map(extractor::extract)
                    .orElse(Collections.emptySet());

            toProcess.addAll(
                    links.stream()
                            .filter(onlyInternalUris())
                            .map(ExtractedUri::getUri)
                            .collect(toSet()));

            System.out.println(visitedLinks);
        }
    }

    private Predicate<? super ExtractedUri> onlyInternalUris() {
        return extractedUri -> extractedUri.getType() == URIType.INTERNAL_LINK;
    }
}

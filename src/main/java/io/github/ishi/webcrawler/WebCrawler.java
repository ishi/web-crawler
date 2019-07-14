package io.github.ishi.webcrawler;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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

            Set<String> links = provider.getContent(candidate)
                    .map(extractor::extract)
                    .orElse(Collections.emptySet());

            toProcess.addAll(links);

            System.out.println(visitedLinks);
        }
    }
}

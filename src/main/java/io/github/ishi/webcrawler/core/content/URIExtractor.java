package io.github.ishi.webcrawler.core.content;

import io.github.ishi.webcrawler.core.model.ExtractedUri;

import java.util.Set;

public interface URIExtractor {
    Set<ExtractedUri> extract(String input);
}

package io.github.ishi.webcrawler;

import java.util.Set;

public interface URIExtractor {
    Set<String> extract(String input);
}

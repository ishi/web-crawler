package io.github.ishi.webcrawler;

import java.util.Optional;

public interface DataProvider {
    Optional<String> getContent(String s);
}

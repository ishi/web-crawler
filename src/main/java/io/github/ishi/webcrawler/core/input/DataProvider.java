package io.github.ishi.webcrawler.core.input;

import java.util.Optional;

public interface DataProvider {
    Optional<String> getContent(String s);
}

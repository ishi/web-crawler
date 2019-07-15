package io.github.ishi.webcrawler.core.output;

import io.github.ishi.webcrawler.core.model.ExtractedUri;

import java.util.Collection;

public interface OutputFormat {
    String format(Collection<ExtractedUri> uris);
}

package io.github.ishi.webcrawler;

import java.util.Collection;

public interface OutputFormat {
    String format(Collection<ExtractedUri> uris);
}

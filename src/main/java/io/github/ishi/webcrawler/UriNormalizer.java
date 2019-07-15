package io.github.ishi.webcrawler;

import static io.github.ishi.webcrawler.ExtractedUri.externalLin;

class UriNormalizer {
    private String baseUrl;

    public UriNormalizer(String baseUrl) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
    }

    public ExtractedUri normalize(ExtractedUri uri) {
        if (!uri.getUri().startsWith(baseUrl)) {
            return externalLin(uri.getUri());
        }
        return uri;
    }
}

package io.github.ishi.webcrawler;

import java.net.URI;
import java.net.URISyntaxException;

import static io.github.ishi.webcrawler.ExtractedUri.externalLink;
import static io.github.ishi.webcrawler.ExtractedUri.malformedLink;

class UriNormalizer {
    private URI baseUrl;

    public UriNormalizer(String baseUrl) throws URISyntaxException {
        this.baseUrl = new URI(baseUrl);
    }

    public ExtractedUri normalize(ExtractedUri extracted) {
        try {
            URI uri = new URI(extracted.getUri());
            uri = baseUrl.normalize().resolve(uri);
            if (!uri.getHost().endsWith(baseUrl.getHost())) {
                return externalLink(uri.toString());
            }
            return extracted.withUri(uri.toString());
        } catch (URISyntaxException e) {
            return malformedLink(extracted.getUri());
        }
    }
}

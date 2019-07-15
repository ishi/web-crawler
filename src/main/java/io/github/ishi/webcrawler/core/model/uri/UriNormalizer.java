package io.github.ishi.webcrawler.core.model.uri;

import io.github.ishi.webcrawler.core.model.ExtractedUri;
import io.github.ishi.webcrawler.core.model.URIType;

import java.net.URI;
import java.net.URISyntaxException;

import static io.github.ishi.webcrawler.core.model.ExtractedUri.externalLink;
import static io.github.ishi.webcrawler.core.model.ExtractedUri.malformedLink;

public class UriNormalizer {
    private URI baseUrl;

    public UriNormalizer(String baseUrl) throws URISyntaxException {
        this.baseUrl = new URI(baseUrl);
    }

    public ExtractedUri normalize(ExtractedUri extracted) {
        try {
            String str = extracted.getUri().replaceAll("#.*$", "");
            URI uri = new URI(str);
            uri = baseUrl.normalize().resolve(uri);
            if (extracted.getType() == URIType.INTERNAL_LINK && !uri.getHost().endsWith(baseUrl.getHost())) {
                return externalLink(uri.toString());
            }
            return extracted.withUri(uri.toString());
        } catch (URISyntaxException e) {
            return malformedLink(extracted.getUri());
        }
    }
}

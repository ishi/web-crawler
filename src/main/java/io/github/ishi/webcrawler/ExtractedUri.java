package io.github.ishi.webcrawler;

import java.util.Objects;

public class ExtractedUri {
    private final URIType type;
    private final String uri;

    private ExtractedUri(URIType type, String uri) {
        this.type = type;
        this.uri = uri;
    }

    public static ExtractedUri internalLink(String uri) {
        return new ExtractedUri(URIType.INTERNAL_LINK, uri);
    }

    public static ExtractedUri staticResource(String uri) {
        return new ExtractedUri(URIType.STATIC_RESOURCE, uri);
    }

    public URIType getType() {
        return type;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "ExtractedUri{" +
                "type=" + type +
                ", uri='" + uri + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractedUri that = (ExtractedUri) o;
        return getType() == that.getType() &&
                Objects.equals(getUri(), that.getUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getUri());
    }
}

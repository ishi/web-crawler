package io.github.ishi.webcrawler;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.github.ishi.webcrawler.ExtractedUri.externalLink;
import static io.github.ishi.webcrawler.ExtractedUri.internalLink;
import static org.assertj.core.api.Assertions.assertThat;

class UriNormalizerTest {

    private final UriNormalizer sut = new UriNormalizer("http://internal/");

    UriNormalizerTest() throws URISyntaxException {
    }

    @Test
    void whenBaseUri_shouldDoNothing() {
        // Given
        ExtractedUri uri = internalLink("http://internal");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(uri);
    }

    @Test
    void whenInternalUri_shouldDoNothing() {
        // Given
        ExtractedUri uri = internalLink("http://internal/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(uri);
    }

    @Test
    void whenInternalSubdomainUri_shouldDoNothing() {
        // Given
        ExtractedUri uri = internalLink("http://subdomain.internal/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(uri);
    }

    @Test
    void whenExternalUri_shouldMarkItAsExternal() {
        // Given
        ExtractedUri uri = internalLink("http://external/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(externalLink("http://external/subpage"));
    }


    @Test
    void whenRelativeUri_shouldAddBaseUri() {
        // Given
        ExtractedUri uri = internalLink("/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(internalLink("http://internal/subpage"));
    }
}
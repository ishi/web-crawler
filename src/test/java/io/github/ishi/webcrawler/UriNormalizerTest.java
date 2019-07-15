package io.github.ishi.webcrawler;

import org.junit.jupiter.api.Test;

import static io.github.ishi.webcrawler.ExtractedUri.externalLin;
import static io.github.ishi.webcrawler.ExtractedUri.internalLink;
import static org.assertj.core.api.Assertions.assertThat;

class UriNormalizerTest {

    private final UriNormalizer sut = new UriNormalizer("http://internal/");

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
    void whenExternalUri_shouldMarkItAsExternal() {
        // Given
        ExtractedUri uri = internalLink("http://external/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(externalLin("http://external/subpage"));
    }
}
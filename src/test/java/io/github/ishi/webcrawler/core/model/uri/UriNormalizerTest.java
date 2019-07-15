package io.github.ishi.webcrawler.core.model.uri;

import io.github.ishi.webcrawler.core.model.ExtractedUri;
import io.github.ishi.webcrawler.core.model.uri.UriNormalizer;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;

import static io.github.ishi.webcrawler.core.model.ExtractedUri.*;
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

    @Test
    void whenStaticContentOnExternalDomain_shouldLeaveItAsStaticResource() {
        // Given
        ExtractedUri uri = staticResource("http://external/subpage");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(staticResource("http://external/subpage"));
    }

    @Test
    void whenUriContainsHash_shouldRemoveIt() {
        // Given
        ExtractedUri uri = staticResource("http://external/subpage#remove-me");

        // When
        ExtractedUri result = sut.normalize(uri);

        // Then
        assertThat(result).isEqualTo(staticResource("http://external/subpage"));
    }
}
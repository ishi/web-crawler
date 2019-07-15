package io.github.ishi.webcrawler.infrastructure.html;

import io.github.ishi.webcrawler.infrastructure.html.HtmlURIExtractor;
import io.github.ishi.webcrawler.core.model.ExtractedUri;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.github.ishi.webcrawler.core.model.ExtractedUri.internalLink;
import static io.github.ishi.webcrawler.core.model.ExtractedUri.staticResource;

class HtmlURIExtractorTest {

    private final HtmlURIExtractor sut = new HtmlURIExtractor();

    @Test
    void whenHyperLinkProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<a href=\"http://test.ling.com/\">link</a>" +
                "</body>" +
                "</html>";

        // When
        Set<ExtractedUri> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of(internalLink("http://test.ling.com/")));
    }

    @Test
    void whenImageProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<img src=\"http://test.img.com/\">" +
                "</body>" +
                "</html>";

        // When
        Set<ExtractedUri> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of(staticResource("http://test.img.com/")));
    }

    @Test
    void whenExternalScriptProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<script src=\"http://test.script.com/\">" +
                "</body>" +
                "</html>";

        // When
        Set<ExtractedUri> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of(staticResource("http://test.script.com/")));
    }

    @Test
    void whenInlineScriptProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<script></script>" +
                "</body>" +
                "</html>";

        // When
        Set<ExtractedUri> result = sut.extract(content);

        // Then
        Assertions.assertThat(result).isEmpty();
    }
}

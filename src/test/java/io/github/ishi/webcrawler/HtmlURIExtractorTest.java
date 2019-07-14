package io.github.ishi.webcrawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.github.ishi.webcrawler.ExtractedUri.internalLink;
import static io.github.ishi.webcrawler.ExtractedUri.staticResource;

public class HtmlURIExtractorTest {

    private final HtmlURIExtractor sut = new HtmlURIExtractor();

    @Test
    public void whenHyperLinkProvided_shouldExtractIt() {
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
    public void whenImageProvided_shouldExtractIt() {
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
    public void whenExternalScriptProvided_shouldExtractIt() {
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
    public void whenInlineScriptProvided_shouldExtractIt() {
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

package io.github.ishi.webcrawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class URIExtractorTest {

    @Test
    public void whenHyperLinkProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<a href=\"http://test.ling.com/\">link</a>" +
                "</body>" +
                "</html>";
        URIExtractor sut = new URIExtractor();

        // When
        Set<String> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of("http://test.ling.com/"));
    }

    @Test
    public void whenImageProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<img src=\"http://test.img.com/\">" +
                "</body>" +
                "</html>";
        URIExtractor sut = new URIExtractor();

        // When
        Set<String> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of("http://test.img.com/"));
    }

    @Test
    public void whenExternalScriptProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<script src=\"http://test.script.com/\">" +
                "</body>" +
                "</html>";
        URIExtractor sut = new URIExtractor();

        // When
        Set<String> result = sut.extract(content);

        // Then
        Assertions.assertThat(result)
                .isEqualTo(Set.of("http://test.script.com/"));
    }


    @Test
    public void whenInlineScriptProvided_shouldExtractIt() {
        // Given
        String content = "<html>" +
                "<body>" +
                "<script></script>" +
                "</body>" +
                "</html>";
        URIExtractor sut = new URIExtractor();

        // When
        Set<String> result = sut.extract(content);

        // Then
        Assertions.assertThat(result).isEmpty();
    }
}
